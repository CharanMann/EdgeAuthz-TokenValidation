/*
 * Copyright © 2020 ForgeRock, AS.
 *
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * EdgeAuthz-TokenValidation: Created by Charan Mann on 10/15/20 , 9:21 PM.
 */

/*
 * Groovy script for checking shared cache for token revocations
 *
 * This script requires these arguments: cacheEndpoint, cacheMap, delegate
 */

@Grab(group = 'org.redisson', module = 'redisson', version = '3.12.0')

import groovy.json.JsonSlurper
import org.forgerock.http.protocol.Response
import org.forgerock.http.protocol.Status
import org.redisson.Redisson
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.redisson.config.Config

import static org.forgerock.http.protocol.Response.newResponsePromise

def getRedisClient() {
    //logger.info("Creating Redis cache: ${cacheEndpoint}")
    Config config = new Config()
    config.useSingleServer().setAddress(cacheEndpoint)

    RedissonClient redissonClient = Redisson.create()
    logger.info("Connected to Redis endpoint: ${cacheEndpoint}")

    redissonClient
}

/**
 * Creates validation failure error
 */
def validationFailure() {
    logger.info("Returning validation failure error")
    Response validationFailureRes = new Response(Status.OK)
    validationFailureRes.entity.json = ["active": false];

    // Need to wrap the response object in a promise
    return newResponsePromise(validationFailureRes)
}

/**
 * Invoke delegate
 */
def invokeDelegate() {
    // Call delegate.
    return delegate.resolve(context, token)
}

logger.info("Processing token: ${token}")
RedissonClient redisClient = globals["${cacheEndpoint}"]

// If redisClient is not present in globals, then create this object
if (!redisClient) {
    redisClient = getRedisClient()
    globals["${cacheEndpoint}"] = redisClient
}

logger.info("Retrieving Redis cache map from Redis server: ${cacheMap}")
RMapCache redisMap = redisClient.getMapCache(cacheMap)

// If there is an object in blacklisted cache, then return failure message
String cachekey
if (token) {

    // Retrieve JWT body and base64 decode
    String decodedBody = new String((token.tokenize(".")[1]).decodeBase64())
    JsonSlurper jsonSlurper = new JsonSlurper()
    def jwtBody = jsonSlurper.parseText(decodedBody)
    logger.info("Decoded JwT body: ${jwtBody}")

    // Retrieve authGrantId field from JWT
    cachekey = jwtBody["authGrantId"]
    logger.info("Checking for key: ${cachekey} in redis cache")
}

if (cachekey && redisMap.get(cachekey)) {

    logger.info("Blacklisted token found in redis cache. This token has been revoked.")
    validationFailure()
} else {

    logger.info("Blacklisted token not found in redis cache, proceeding with StatelessAccessTokenResolver")
    invokeDelegate()
}



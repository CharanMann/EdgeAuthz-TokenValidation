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
 * Groovy script for updating shared cache with token revocations
 *
 * This script requires these arguments: cacheEndpoint, cacheSet
 */

@Grab(group = 'org.redisson', module = 'redisson', version = '3.12.0')

import groovy.json.JsonSlurper
import org.redisson.Redisson
import org.redisson.api.RSetCache
import org.redisson.api.RedissonClient
import org.redisson.config.Config

def getRedisClient() {
    //logger.info("Creating Redis cache: ${cacheEndpoint}")
    Config config = new Config()
    config.useSingleServer().setAddress(cacheEndpoint)

    RedissonClient redissonClient = Redisson.create()
    logger.info("Connected to Redis endpoint: ${cacheEndpoint}")

    redissonClient
}

/**
 * Invoke next handler
 */
def invokeNextHandler() {
    // Call the next handler. This returns when the request has been handled.
    return next.handle(context, request)
}

def token = request.form["token"][0]
logger.info("Processing token: ${token}")

RedissonClient redisClient = globals["${cacheEndpoint}"]

// If redisClient is not present in globals, then create this object
if (!redisClient) {
    redisClient = getRedisClient()
    globals["${cacheEndpoint}"] = redisClient
}

logger.info("Retrieving Redis cache set from Redis server: ${cacheSet}")
RSetCache redisSet = redisClient.getSetCache(cacheSet)

String cachekey
if (token) {

    // Retrieve JWT body and base64 decode
    String decodedBody = new String((token.tokenize(".")[1]).decodeBase64())
    JsonSlurper jsonSlurper = new JsonSlurper()
    def jwtBody = jsonSlurper.parseText(decodedBody)
    logger.info("Decoded JwT body: ${jwtBody}")

    // Retrieve authGrantId field from JWT
    cachekey = jwtBody["authGrantId"]
    logger.info("Adding key: ${cachekey} in redis cache")
    redisSet.add(cachekey)
} else {
    // If there is no token in request, then log message and proceed to next filter
    logger.info("No token found in the request.")
}

invokeNextHandler()



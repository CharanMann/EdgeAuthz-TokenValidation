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
 * EdgeAuthz-TokenValidation: Created by Charan Mann on 1/30/20 , 9:21 PM.
 */

/*
 * Groovy script for checking shared cache for token revocations
 *
 * This script requires these arguments: cacheEndpoint, cacheSet, delegate
 */

@Grab(group='org.redisson', module='redisson', version='3.12.0')
import org.forgerock.http.protocol.Response
import org.forgerock.http.protocol.Status
import org.redisson.api.RSetCache
import org.redisson.api.RedissonClient
import org.redisson.config.Config

import static org.forgerock.http.protocol.Response.newResponsePromise

def getSetCache() {
    //logger.info("Creating Redis cache: ${cacheEndpoint}")
    Config config = new Config()
    config.useSingleServer().setAddress(cacheEndpoint)

    RedissonClient redissonClient = org.redisson.Redisson.create()

    logger.info("Retrieving Redis cache set: ${cacheSet}")
    redissonClient.getSetCache(cacheSet)
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
    // Call the next handler. This returns when the request has been handled.
    return delegate.resolve(context, token)
}

RSetCache redisSet = getSetCache()

// If there is an object in blacklisted cache, then return error
logger.info("Checking ${token} in redis cache")

if (redisSet.contains(token)) {
    validationFailure()
} else {
    invokeDelegate()
}



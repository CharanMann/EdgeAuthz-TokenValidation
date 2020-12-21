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
 * EdgeAuthz-TokenValidation: Created by Charan Mann on 1/31/20 , 5:42 PM.
 */

import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisTest {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://fr7-aws.example.com:6379");

        RedissonClient redissonClient = org.redisson.Redisson.create(config);

        RSetCache<String> rSetCache = redissonClient.getSetCache("tokenSet");

        // Add authGrantId
        rSetCache.add("LZmUJtEx1XhE0tKtCb5t3JYl79c.Y6uFkeDcKmC38kiVOGzf0HClZjc");

        redissonClient.shutdown();
    }
}

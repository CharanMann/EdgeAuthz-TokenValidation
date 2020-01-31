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
        config.useSingleServer().setAddress("redis://192.168.56.132:6379");

        RedissonClient redissonClient = org.redisson.Redisson.create(config);

        RSetCache rSetCache = redissonClient.getSetCache("tokenSet");

        rSetCache.add("eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoid1UzaWZJSWFMT1VBUmVSQi9GRzZlTTFQMVFNPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJtaWNyb3NlcnZpY2UtY2xpZW50IiwiY3RzIjoiT0FVVEgyX0dSQU5UX1NFVCIsImF1ZGl0VHJhY2tpbmdJZCI6IjdhMTc2Zjk0LTc0YzUtNDNiMC1iN2I4LTRiNjJkNzc3MDQwZi0xMDMwMjIiLCJpc3MiOiJodHRwOi8vYW02NTEuZXhhbXBsZS5jb206ODA4Ni9hbS9vYXV0aDIvZW1wbG95ZWVzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoidWQtOWFiUUthLTJ1TXJ5aWtNbEJPdVlzWUJBLmRoRE1GczNUTkQtY25iZ3FCZTEwOWtUdlhIOCIsImF1ZCI6Im1pY3Jvc2VydmljZS1jbGllbnQiLCJuYmYiOjE1ODA0NDg4OTUsImdyYW50X3R5cGUiOiJjbGllbnRfY3JlZGVudGlhbHMiLCJzY29wZSI6WyJtaWNyb3NlcnZpY2UtQSIsImNsaWVudC1zY29wZSJdLCJhdXRoX3RpbWUiOjE1ODA0NDg4OTUsInJlYWxtIjoiL2VtcGxveWVlcyIsImV4cCI6MTU4MDQ1MjQ5NSwiaWF0IjoxNTgwNDQ4ODk1LCJleHBpcmVzX2luIjozNjAwLCJqdGkiOiJ1ZC05YWJRS2EtMnVNcnlpa01sQk91WXNZQkEuNlBLTldRUmZBVkRXWlpNSHY5dkwwVnlxSkF3In0.rfxugzhmgvFhOB4JApbyd78ZdWNOQ5GP3OZHbtv0EvheNdQSWDDnBXl6BubavR-kYXm6BOPCG6Ucwkpb07YlvZgodGGTpPdUU43ZUgNwVQIGlfaP9AYYVEEwbwRE9uB2grszY9QLc4BTk3o5LRefgz7pofg2YN9Hs_SQrlHFWFAfBIPFKyxxXub1NjV85QmX3j2I29bZ5TF1YM4lcRK6-ez0JkTeGZQwUBPzLnQll5LZZZ1ELWUsygLO35G98Uk6Rp0iuD9YXLUjlYWp-Er9dz7aqTO4MKBOi6qoeasMZ1gCVjgiptNccP9TRIBbHlauBoWVnCTcljPy2h8et17RGQ");
    }
}

# EdgeAuthz-TokenValidation

A Såhared Denylist Cache solution for securely and efficiently decoupling microservices from OAuth Authorization server. <br />

Disclaimer of Liability :
=========================
The sample code described herein is provided on an "as is" basis, without warranty of any kind, to the fullest extent
permitted by law. ForgeRock does not warrant or guarantee the individual success developers may have in implementing the
sample code on their development platforms or in production configurations.

ForgeRock does not warrant, guarantee or make any representations regarding the use, results of use, accuracy,
timeliness or completeness of any data or information relating to the sample code. ForgeRock disclaims all warranties,
expressed or implied, and in particular, disclaims all warranties of merchantability, and warranties related to the
code, or any service or software related thereto.

ForgeRock shall not be liable for any direct, indirect or consequential damages or costs of any type arising out of any
action taken by you or others related to the sample code.

Pre-requisites :
================
* Versions used for this project: IG 7.0.1, AM 7.0.0, MicroserviceTokenValidation-1.0.2

1. Redis has been installed for shared caching service. This cache needs to be reachable from AM and MicroserviceTokenValidation
2. AM has been installed and configured. 

AM Configuration:
=====================
1. Create realm /customers and enable OAuth Provider
2. Enable Client-Based OAuth 2.0 tokens for this provider
3. Create OAuth clients: microservice-client
4. Add microservice-A scope to this client
5. Enable appropriate grant types such as ROPC, refresh token etc 

IG (Acting as AM Reverse Proxy) Configuration:
=====================
1. Install IG
2. Leverage configs under /ig-am for this deployment, update these configs as needed. 
3. Configure ttl in ig-am/config/routes/02-am-oauth-revoke.json route. This value defines token expiry in cache. Its recommended to configure this value same as Refresh Token Lifetime in AM's OAuth2 provider.    
4. Start server: ig/bin/start.sh ~/forgerock/ig-am/

IG (Acting as OAuth-RS) Configuration:
=====================
1. Install IG
2. Leverage configs under /ig-oauth-rs for this deployment, update these configs as needed.
3. Start server: ig/bin/start.sh ~/forgerock/ig-oauth-rs/
4. For more details, refer https://backstage.forgerock.com/docs/ig/7/gateway-guide/oauth2-rs.html 

MicroserviceTokenValidation Configuration:
=====================
1. Install MicroserviceTokenValidation
2. Leverage configs under /token-validation for this deployment, update these configs as needed.
3. Start server: tvms1/bin/start.sh ~/forgerock/token-validation/
4. For more details, refer https://backstage.forgerock.com/docs/mg/1/user-guide/#chap-using
   

Testing:
=========================
* Refer ![ScreenShot](./diagrams/EdgeAuthzSequence.png) for complete flow
* Refer postmanCollection/Edge Authz.postman_collection.json for postman collection:
   - Valid token flow 
   - Invalid token flow  


* * *

The contents of this file are subject to the terms of the Common Development and Distribution License (the License). You
may not use this file except in compliance with the License.

You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the specific language governing
permission and limitations under the License.

When distributing Covered Software, include this CDDL Header Notice in each file and include the License file at
legal/CDDLv1.0.txt. If applicable, add the following below the CDDL Header, with the fields enclosed by brackets []
replaced by your own identifying information: "Portions copyright [year] [name of copyright owner]".

Copyright 2020 ForgeRock AS.


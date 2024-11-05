<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->


<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/Great-Side-Projects/scim-service-server">
    <img src="https://scim.cloud/img/logo/SCIM_B-and-W_72x24.png" alt="Logo" width="200">
  </a>

<h3 align="center">SCIM Service Server</h3>

  <p align="center">
    An awesome API to implement SCIM standard for user management 
    <br />
    <a href="https://scim.cloud/"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://scim-service-server.azurewebsites.net/">View Demo</a>
    ·
    <a href="https://github.com/Great-Side-Projects/scim-service-server/issues">Report Bug</a>
    ·
    <a href="https://github.com/Great-Side-Projects/scim-service-server/issues/new">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
        <li><a href="#Architecture-design">Architecture design</a></li>
        <li><a href="#Architecture-diagram">Architecture diagram</a></li>
        <li><a href="#URL-Shortening-Algorithm">URL Shortening Algorithm</a></li>
     </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://scim-service-server.azurewebsites.net/swagger-ui/index.html#/)
[![Product Name Screen Shot][product-screenshot-UI]](https://scim-service-server.azurewebsites.net/#)
[![Product Name Screen Shot][product-screenshot-UI2]](https://scim-service-server.azurewebsites.net/#)
[![Product Name Screen Shot][product-screenshot-UI3]](https://scim-service-server.azurewebsites.net/#)
[![Product Name Screen Shot][product-screenshot-UI4]](https://scim-service-server.azurewebsites.net/#)
[![Product Name Screen Shot][product-screenshot-UI5]](https://scim-service-server.azurewebsites.net/#)

The System for Cross-domain Identity Management (SCIM) specification is designed to make managing user identities in cloud-based applications and services easier. The specification suite seeks to build upon experience with existing schemas and deployments, placing specific emphasis on simplicity of development and integration, while applying existing authentication, authorization, and privacy models. Its intent is to reduce the cost and complexity of user management operations by providing a common user schema and extension model, as well as binding documents to provide patterns for exchanging this schema using standard protocols. In essence: make it fast, cheap, and easy to move users in to, out of, and around the cloud.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* [![Java][java.com/es/]][Java-url]
* [![Spring Boot][Spring Boot]][Spring Boot-url]
* [![MySQL][mysql.com]][MySQL-url]
* [![Audit with Spring Data JPA][Audit with Spring Data JPA]][Audit with Spring Data JPA-url]
* [![Bootstrap][Bootstrap.com]][Bootstrap-url]
* [![JQuery][JQuery.com]][JQuery-url]
* [![Thymeleaf][Thymeleaf.com]][Thymeleaf-url]

### Architecture design

The project is designed with spring web rest API and design pattern like Template, Strategy, and Factory. The project is divided into three layers: controller, service, and repository with SOLID principles. The controller layer is responsible for receiving the requests and sending the responses. The service layer is responsible for the business logic and the repository layer is responsible for the database operations.

### Design Patterns 

- Template Method: 
- Strategy:
- Factory:

Entity field mapper diagram - Template Method + Pattern Strategy

![Mapper Field Diagram][Mapper-Field-Classes-Diagram]

Entity mapper diagram - Factory + Pattern Strategy

![Mapper Patch Operations Diagram][Mapper-Patch-Operations-Diagram]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

Currently supported endpoints are User and Group: 
- POST /Users

Example body:
```json
{
   "schemas": [
      "urn:ietf:params:scim:schemas:core:2.0:User",
      "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User"
   ],
   "externalId": "cobi",
   "userName": "cobi@onmicrosoft.com",
   "active": true,
   "addresses": [
      {
         "primary": false,
         "type": "work",
         "country": "pais o region",
         "formatted": "ubicacion de la oficina",
         "locality": "cuidad update",
         "postalCode": "codigo postal",
         "region": "estadio o providencia",
         "streetAddress": "direccion"
      }
   ],
   "displayName": "Cobi Brass",
   "emails": [
      {
         "primary": true,
         "type": "work",
         "value": "cobi@onmicrosoft.com"
      },
      {
         "primary": false,
         "type": "other",
         "value": "cobi@onmicrosoft.com2"
      }
   ],
   "meta": {
      "resourceType": "User"
   },
   "name": {
      "formatted": "Cobi Brass",
      "familyName": "Brass",
      "givenName": "Cobi"
   },
   "phoneNumbers": [
      {
         "primary": false,
         "type": "fax",
         "value": "numero fax"
      },
      {
         "primary": false,
         "type": "mobile",
         "value": "telefono movil"
      },
      {
         "primary": true,
         "type": "work",
         "value": "telefono de empresa"
      }
   ],
   "title": "puesto",
   "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User": {
      "department": "departamento",
      "employeeNumber": "id empleado"
   }
}
```

- GET /Users

Example request:
```
GET /scim/v2/Users?filter=userName eq "angelm@test.co"&startIndex=1&count=100
```

- GET /Users/{id}

Example request:
```
GET /scim/v2/Users/27f1f25b-6403-4d6d-b8fe-f25ca0d11d1b
```

- PUT /Users/{id}
``` 
PUT /scim/v2/Users/25646a31-3ce7-419e-a432-6d75444f29a6
```
Example body:
```json
{
   "emails": [
      {
         "type": "work",
         "value": "angelm@test.co",
         "primary": true
      }
   ],
   "meta": {
      "location": "/scim/v2/Users/8acc5c58-2e67-47f3-8862-bcf71e4feb75",
      "resourceType": "User"
   },
   "schemas": [
      "urn:ietf:params:scim:schemas:core:2.0:User",
      "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User"
   ],
   "name": {
      "displayName": "displayName Modificado",
      "familyName": "Morales Modificado",
      "givenName": "Angel Modificado",
      "middleName": "cambeo Modificado",
      "honorificPrefix": "prefix",
      "honorificSuffix": "suffix"
   },
   "active": true,
   "id": "8acc5c58-2e67-47f3-8862-bcf71e4feb75",
   "userName": "angelm@test.co",
   "title": "title Modificado",
   "displayName": "displayName Modificado",
   "nickName": "nickName Modificado",
   "profileUrl": "profileUrl Modificado",
   "phoneNumbers": [
      {
         "primary": true,
         "value": "primaryPhone Modificado",
         "type": "work"
      }
   ],
   "addresses": [
      {
         "primary": true,
         "type": "work",
         "streetAddress": "streetAddress Modificado",
         "locality": "city Modificado",
         "region": "state Modificado",
         "postalCode": "zipCode Modificado",
         "country": "AF",
         "formatted": "postalAddress Modificado"
      }
   ],
   "locale": "en-US",
   "timezone": "UTC",
   "userType": "userType Modificado",
   "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User": {
      "employeeNumber": "employeeNumber Modificado",
      "costCenter": "costCenter Modificado",
      "organization": "organization Modificado",
      "division": "division Modificado",
      "department": "department Modificado",
      "manager": {
         "value": "managerId Modificado",
         "displayName": "manager Modificado"
      }
   },
   "externalId": "00u5w9ea45DGmErpU5d7",
   "groups": []
}
```

- PATCH /Users/{id}
```
PATCH /scim/v2/Users/a75be5cb-b9a9-4cfe-bd04-97a1e384f280
```
Example body:
```json
{
   "schemas": [
      "urn:ietf:params:scim:api:messages:2.0:PatchOp"
   ],
   "Operations": [
      {
         "op": "Replace",
         "path": "active",
         "value": "False"
      },
      {
         "op": "Add",
         "path": "displayName",
         "value": "Cobi Brass"
      },
      {
         "op": "Add",
         "path": "title",
         "value": "puesto"
      },
      {
         "op": "Add",
         "path": "name.formatted",
         "value": "Cobi Brass"
      },
      {
         "op": "Add",
         "path": "addresses[type eq \"work\"].formatted",
         "value": "ubicacion de la oficina"
      },
      {
         "op": "Add",
         "path": "addresses[type eq \"work\"].streetAddress",
         "value": "direccion"
      },
      {
         "op": "Add",
         "path": "addresses[type eq \"work\"].locality",
         "value": "cuidad update"
      },
      {
         "op": "Add",
         "path": "addresses[type eq \"work\"].region",
         "value": "estadio o providencia"
      },
      {
         "op": "Add",
         "path": "addresses[type eq \"work\"].postalCode",
         "value": "codigo postal"
      },
      {
         "op": "Add",
         "path": "addresses[type eq \"work\"].country",
         "value": "pais o region"
      },
      {
         "op": "Add",
         "path": "phoneNumbers[type eq \"work\"].value",
         "value": "telefono de empresa"
      },
      {
         "op": "Add",
         "path": "phoneNumbers[type eq \"mobile\"].value",
         "value": "telefono movil"
      },
      {
         "op": "Add",
         "path": "phoneNumbers[type eq \"fax\"].value",
         "value": "numero fax"
      },
      {
         "op": "Add",
         "path": "externalId",
         "value": "cobi"
      },
      {
         "op": "Add",
         "path": "emails[type eq \"other\"].value",
         "value": "cobi@.onmicrosoft.com2"
      },
      {
         "op": "Add",
         "path": "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:employeeNumber",
         "value": "id empleado"
      },
      {
         "op": "Add",
         "path": "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:department",
         "value": "departamento"
      }
   ]
}
```

- DELETE /Users/{id}
```
DELETE /scim/v2/Users/27f1f25b-6403-4d6d-b8fe-f25ca0d11d1b
```

- GET /Groups

Example request:
```
GET /scim/v2/Groups?filter=displayName eq "prueba_gropup"&startIndex=1&count=100&excludedAttributes=members
```

- POST /Groups

Example body:
```json
{
   "schemas": [
      "urn:ietf:params:scim:schemas:core:2.0:Group"
   ],
   "displayName": "prueba_gropup",
   "members": [
      {
         "value": "4bbca18b-8b08-40d8-abec-9040e601cb04",
         "display": "angelm@test.co"
      },
      {
         "value": "925f7c9c-3a57-4881-9c72-bfb766240148",
         "display": "pruebasdev@prueba.com"
      }
   ]
}
```

- GET /Groups/{id}

Example request:
```
GET /scim/v2/Groups/58949d1f-dba6-4e2a-83af-f937aa30627d?excludedAttributes=members
```

- PUT /Groups/{id}

```
PUT /scim/v2/Groups/0385ac95-8ea3-4869-bad4-4ad84da39442
```

Example body:
```json
{
	"schemas": [
		"urn:ietf:params:scim:schemas:core:2.0:Group"
	],
	"id": "0385ac95-8ea3-4869-bad4-4ad84da39442",
	"displayName": "prueba_gropup",
	"members": [
		{
			"value": "393c30b2-9009-407b-b1bc-84f9abe5652a",
			"display": "angelm@test.co"
		},
		{
			"value": "5fa20e40-2e0f-4672-bee3-c0d7ca120b1e",
			"display": "pruebasdev@prueba.com"
		}
	]
}
```       
          
- PATCH /Groups/{id}

```
PATCH /scim/v2/Groups/0385ac95-8ea3-4869-bad4-4ad84da39442
```
```json
{
   "schemas": [
      "urn:ietf:params:scim:api:messages:2.0:PatchOp"
   ],
   "Operations": [
      {
         "op": "Remove", // Add, Remove
         "path": "members",
         "value": [
            {
               "value": "70dea213-4a3b-4675-bc73-964a21ada135"
            },
            {
               "value": "afd4e55b-9208-40c8-bb76-e53fe4d0c14f"
            }
         ]
      }
   ]
}
```
or
```json
{
   "schemas": [
      "urn:ietf:params:scim:api:messages:2.0:PatchOp"
   ],
   "Operations": [
      {
         "op": "Replace",
         "path": "members",
         "displayName": "prueba_gropup"
      }
   ]
}
```

- DELETE /Groups/{id}

```
DELETE /scim/v2/Groups/0385ac95-8ea3-4869-bad4-4ad84da39442
```


This project was testing with the following IDPs with User and Group provisioning:
- Okta
- Azure AD
<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Architecture diagram
[![Architecture diagram][architecture-diagram]](https://scim-service-server.azurewebsites.net/)

## Basic User Schema
Map the user schemaSCIM vs database schema vs scim-service-server.

Substitute this name en el field table

**schemavalue=urn:ietf:params:scim:schemas:extension:enterprise:2.0

the blank fields are not implemented yet.
 
| User Schema SCIM                     | Database users  | scim-service-server      |
|--------------------------------------|-----------------|--------------------------|
| schemas                              |                 | Hardcode                 |
| id                                   | id              | randomUUID();            |
| externalId                           |                 |                          |
| userName                             | user_name       |                          |
| name.formatted                       |                 |                          |
| name.familyName                      | family_name     |                          |
| name.givenName                       | given_name      |                          |
| name.middleName                      | middle_name     |                          |
| name.honorificPrefix                 |                 |                          |
| name.honorificSuffix                 |                 |                          |
| displayName                          | display_name    |                          |
| nickName                             | nick_name       |                          |
| profileUrl                           | profile_url     |                          |
| title                                | title           |                          |
| userType                             |                 |                          |
| preferredLanguage                    |                 |                          |
| locale                               | locale          |                          |
| timezone                             |                 |                          |
| active                               | active          |                          |
| password                             |                 |                          |
| email.value                          | email           | email.primary=true       |
| email.value                          | second_email    | email.type=other         |
| phoneNumber.value                    | primary_phone   | phoneNumber.primary=true |
| phoneNumber.value                    | mobile_phone    | phoneNumber.type=mobile  |
| phoneNumber.value                    | business_phone  | phoneNumber.type=work    |
| address.primary                      |                 |                          |
| address.formatted                    | postal_address  | addresses.type=work      |
| address.streetAddress                | street_address  | addresses.type=work      |
| address.locality                     | locality        | addresses.type=work      |
| address.region                       | region          | addresses.type=work      |
| address.postalCode                   | postal_code     | addresses.type=work      |
| address.country                      | country         | addresses.type=work      |
| schemavalue:User.employeeNumber      | employee_number |                          |
| schemavalue:User.costCenter          |                 |                          |
| schemavalue:User.organization        | organization    |                          |
| schemavalue:User.division            | division        |                          |
| suschemavalue:User.department        | department      |                          |
| schemavalue:User.manager.displayName | manager         |                          |
| schemavalue:User.manager.value       |                 |                          |
| meta.resourceType                    |                 |                          |
| meta.created                         |                 |                          |
| meta.lastModified                    |                 |                          |
| meta.location                        |                 |                          |
| meta.version                         |                 |                          |

**Note:** Your SCIM API can use anything as an `id`, provided that the `id`
uniquely identifies reach resource, as described in [section 3.1](https://tools.ietf.org/html/rfc7643#section-3.1) of
[RFC 7643](https://tools.ietf.org/html/rfc7643).

Finally, your SCIM API must also support marking a resource as
"active" or "inactive."

In our sample application, each user resource has a Boolean
"active" attribute which is used to mark a user resource as
"active" or "inactive":
```java
    @Column(columnDefinition="boolean default false")
    public Boolean active = false;
```
## Create Account: POST /Users

An HTTP POST to the `/Users` endpoint must return an immutable or
system ID of the user (`id`) must be returned.

For more information on user creation via the `/Users` SCIM
endpoint, see [section 3.3](https://tools.ietf.org/html/rfc7644#section-3.3) of the [SCIM 2.0 Protocol Specification](https://tools.ietf.org/html/rfc7644).

## Read list of accounts with search: GET /Users

For more details on the `/Users` SCIM endpoint, see [section 3.4.2](https://tools.ietf.org/html/rfc7644#section-3.4.2)
of the [SCIM 2.0 Protocol Specification](https://tools.ietf.org/html/rfc7644).

## Read Account Details: GET /Users/{id}

If we don't find a user, we return a HTTP status 404 ("Not found")
with SCIM error message.

For more details on the `/Users/{id}` SCIM endpoint, see [section 3.4.1](https://tools.ietf.org/html/rfc7644#section-3.4.1)
of the [SCIM 2.0 Protocol Specification](https://tools.ietf.org/html/rfc7644).

## Update Account Details: PUT /Users/{id}

For more details on updates to the `/Users/{id}` SCIM endpoint, see [section 3.5.1](https://tools.ietf.org/html/rfc7644#section-3.5.1)
of the [SCIM 2.0 Protocol Specification](https://tools.ietf.org/html/rfc7644).

## Deactivate Account: PATCH /Users/{id}

For more details on user attribute updates to `/Users/{id}` SCIM endpoint, see [section 3.5.2](https://tools.ietf.org/html/rfc7644#section-3.5.2)
of the [SCIM 2.0 Protocol Specification](https://tools.ietf.org/html/rfc7644).

## Filtering on `id`, `userName`, and `emails`

Examples of filters SCIM API are as
follows:

> userName eq "jhon@doe.com"

> emails eq "doe@doe.com"

For more details on filtering in SCIM 2.0, see [section 3.4.2.2](https://tools.ietf.org/html/rfc7644#section-3.4.2.2)
of the [SCIM 2.0 Protocol Specification](https://tools.ietf.org/html/rfc7644).

## Resource Paging

Note: This code subtracts "1" from the `startIndex` and `count`, because `startIndex` is [1-indexed](https://tools.ietf.org/html/rfc7644#section-3.4.2).
Below is an example of a `curl` command that makes a request to the
`/Users/` SCIM endpoint with `count` and `startIndex` set:
```
    $ curl 'https://scim-service-server.azurewebsites.net/scim/v2/Users?count=1&startIndex=1'
    {
    "totalResults": 2,
    "startIndex": 0,
    "itemsPerPage": 1,
    "schemas": [
        "urn:ietf:params:scim:api:messages:2.0:ListResponse"
    ],
    "Resources": [
        {
            "emails": [
                {
                    "type": "work",
                    "value": "angelm@test.co",
                    "primary": true
                }
            ],
            "meta": {
                "location": "/scim/v2/Users/27f1f25b-6403-4d6d-b8fe-f25ca0d11d1b",
                "resourceType": "User"
            },
            "schemas": [
                "urn:ietf:params:scim:schemas:core:2.0:User"
            ],
            "name": {
                "displayName": "manager Modificado",
                "familyName": "Morales Modificado",
                "givenName": "Angel Modificado 2",
                "middleName": "cambeo Modificado"
            },
            "active": true,
            "id": "27f1f25b-6403-4d6d-b8fe-f25ca0d11d1b",
            "userName": "angelm@test.co"
        }
    ]
}
```
Note: This code subtracts "1" from the
`startIndex` and `count`, because `startIndex` is [1-indexed](https://tools.ietf.org/html/rfc7644#section-3.4.2).

For more details pagination on a SCIM 2.0 endpoint, see [section 3.4.2.4](https://tools.ietf.org/html/rfc7644#section-3.4.2.4)
of the [SCIM 2.0 Protocol Specification](https://tools.ietf.org/html/rfc7644).

### Rate Limiting - Roadmap

For more details on rate limiting transactions using the HTTP 429
status code, see [section 4](https://tools.ietf.org/html/rfc6585#section-4) of [RFC 6585](https://tools.ietf.org/html/rfc6585)

### "/Me" Authenticated Subject Alias - Roadmap

The `/Me` URI alias for the current authenticated subject is
covered in
[section 3.11](https://tools.ietf.org/html/rfc7644#section-3.11) of the [SCIM 2.0 Protocol Specification](https://tools.ietf.org/html/rfc7644).

### /Schemas API endpoint - Roadmap

Here is the specification for the `/Schemas` endpoint, from
[section 4](https://tools.ietf.org/html/rfc7644#section-4) of [RFC 7644](https://tools.ietf.org/html/rfc7644):

An HTTP GET to this endpoint is used to retrieve information about
> resource schemas supported by a SCIM service provider.  An HTTP
> GET to the endpoint "/Schemas" SHALL return all supported schemas
> in ListResponse format (see Figure 3).  Individual schema
> definitions can be returned by appending the schema URI to the
> /Schemas endpoint.  For example:
>
> /Schemas/urn:ietf:params:scim:schemas:core:2.0:User
>
> The contents of each schema returned are described in Section 7 of
> RFC7643.  An example representation of SCIM schemas may be found
> in Section 8.7 of RFC7643.

### /ServiceProviderConfig API endpoint - Roadmap

Here is the specification for the `/ServiceProviderConfig` endpoint, from
[section 4](https://tools.ietf.org/html/rfc7644#section-4) of [RFC 7644](https://tools.ietf.org/html/rfc7644):

> An HTTP GET to this endpoint will return a JSON structure that
> describes the SCIM specification features available on a service
> provider.  This endpoint SHALL return responses with a JSON object
> using a "schemas" attribute of
> "urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig".
> The attributes returned in the JSON object are defined in
> Section 5 of RFC7643.  An example representation of SCIM service
> provider configuration may be found in Section 8.5 of RFC7643.

### Integration with External Systems
@PostPersist and @PostUpdate annotations are used to notify the creation and update of a user.
The SCIM API must be able to integrate with external systems.

https://www.baeldung.com/database-auditing-jpa
```java
  public class UserListener {

   private final static Logger logger = LoggerFactory.getLogger(UserListener.class);

   @PostPersist
   private void afterCreation(User user) {
      //Todo: connect to external service to notify the creation of a new user
      System.out.println("User Created: id:" + user.id);
      logger.info("User Created: id:" + user.id);
   }

   @PostUpdate
   private void afterUpdate(User user) {
      //Todo: connect to external service to notify the update of a user
      if (!user.getStatusChanged()) {
         System.out.println("There are no changes in the user's status. id:" + user.id);
         logger.info("There are no changes in the user's status. id:" + user.id);
         return;
      }

      if (user.active) {
         System.out.println("User Activated: id:" + user.id);
         logger.info("User Activated: id:" + user.id);
      }
      if (!user.active) {
         System.out.println("User Deactivated: id:" + user.id);
         logger.info("User Deactivated: id:" + user.id);
      }
   }
}
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

Here you can find the steps to run the project in your local environment to explore the API and the simple UI.

### Prerequisites

This is an example of how to list things you need to use the software and how to install them.

* Java 22+
* Maven
* Docker
* MySQL local or remote

### Installation


1. Clone the repo
   ```sh
   git clone https://github.com/Great-Side-Projects/scim-service-server.git
   ```
2. Go to the root folder of the project
   ```sh
   cd scim-service-server
   ```
3. Generate the jar file. root folder of the project or use the IDE, verify jar file is target/*.jar.
   ```sh
   mvn clean install
   ```
4. Create the database in MySQL
   ```sh
   CREATE DATABASE scim;
   ``` 
5. Create image and run with docker. root folder of the project
   ```sh
    docker build --build-arg DATASOURCE_URL=jdbc:mysql://localhost:3306/scim --build-arg DATASOURCE_USERNAME=root --build-arg DATASOURCE_PASSWORD=root 
   ```
6. Run the docker image
   ```sh
   docker run -p 8080:8080 -t scim-service-server
   ```
7. Open your browser and go to `http://localhost:8080/swagger-ui/index.html#/` to see the API documentation or `http://localhost:8080/#` to see the simple UI to manage the users.
8. Enjoy!

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage

### Easy way:
Go to `http://localhost:8080/` and you can see the simple UI to manage the URLs.

### Medium way:
Go to `http://localhost:8080/swagger-ui/index.html#/` and you can see the API documentation to manage the URLs.

### Pro way:
Implement in IDPs like Okta, Azure AD, or any other IDP that supports SCIM standard 2.0.

- https://developer.okta.com/docs/guides/scim-provisioning-integration-connect/main/
- https://learn.microsoft.com/en-us/azure/databricks/admin/users-groups/scim/aad

_For more examples, please refer to the [Documentation](https://scim-service-server.azurewebsites.net)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ROADMAP -->
## Roadmap

- [x] Understand SCIM standard
- [x] Implement SCIM standard
- [x] Implement simple UI to manage the users
- [x] Implement Docker
- [x] Implement Swagger
- [x] Implement SonarQube
- [x] Implement CI/CD pipeline
- [x] Implement unit tests
- [x] Implement integration tests
- [x] Implement audit
- [ ] Export option to substitute Map<> objects in the endpoints
- [ ] Implement Authentication and Authorization for security
- [ ] health check
- [ ] Refactor toScimResource method entities
- [ ] Add Schema endpoint
- [ ] Implement resiliency patterns like Circuit Breaker and Retry
- [ ] Implement redis for cache
- [ ] Refactor UI no load all data at once (pagination)
- [ ] Implement service discovery and registry
- [ ] Implement UI manage authentication and authorization for security
- [ ] Implement tracing and logging for monitoring and troubleshooting
- [ ] Implement metrics and monitoring
- [ ] Implement Rate limiting such as adding hundreds of users at once [HTTP 429 Too Many Requests](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes#429)
- [ ] Validate implement "/Me" Authenticated Subject Alias
- [ ] Validate implement "/Schemas" API endpoint
- [ ] Validate implement "/ServiceProviderConfig" API endpoint

See the [open issues](https://github.com/Great-Side-Projects/scim-service-server/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the "develop" Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Angel Morales - [LinkedIn](https://www.linkedin.com/in/angelmoralesb/) - angelmoralesb@gmail.com

Project Link: [https://github.com/Great-Side-Projects/scim-service-server](https://github.com/Great-Side-Projects/scim-service-server)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [Choose an Open Source License](https://choosealicense.com)
* [Docker](https://www.docker.com/)
* [Swagger Spring REST API](https://www.baeldung.com/spring-rest-openapi-documentation)
* [SonnarQube comunity Edition](https://www.sonarsource.com/open-source-editions/sonarqube-community-edition/)
* [GitHub Actions](https://docs.github.com/es/actions)
* [Sonarqube Docker Web App on Linux with Azure SQL](https://learn.microsoft.com/en-us/samples/azure/azure-quickstart-templates/webapp-linux-sonarqube-azuresql/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/Great-Side-Projects/scim-service-server/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/Great-Side-Projects/scim-service-server/forks
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/Great-Side-Projects/scim-service-server/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/Great-Side-Projects/scim-service-server/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/Great-Side-Projects/scim-service-server/blob/main/LICENSE
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/angelmoralesb/
[product-screenshot]: images/screenshot.png
[product-screenshot-UI]: images/screenshotUI.png
[product-screenshot-UI2]: images/screenshotUI2.png
[product-screenshot-UI3]: images/screenshotUI3.png
[product-screenshot-UI4]: images/screenshotUI4.png
[product-screenshot-UI5]: images/screenshotUI5.png
[architecture-diagram]: images/Design%20Architecture%20SCIM%20Service%20Server.drawio.svg
[Mapper-Field-Classes-Diagram]: images/Mapper_Strategy_TemplateMethod.png
[Mapper-Patch-Operations-Diagram]: images/Mapper_Factory_Strategy.png
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com
[java.com/es/]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white
[Java-url]: https://www.java.com/es/
[Spring Boot]: https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot
[Spring Boot-url]: https://spring.io/projects/spring-boot
[mysql.com]: https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white
[MySQL-url]: https://www.mysql.com
[Audit with Spring Data JPA]: https://img.shields.io/badge/Audit_with_Spring_Data_JPA-005F0F?style=for-the-badge&logo=spring&logoColor=white
[Audit with Spring Data JPA-url]: https://www.baeldung.com/database-auditing-jpa
[Thymeleaf.com]: https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white
[Thymeleaf-url]: https://www.thymeleaf.org
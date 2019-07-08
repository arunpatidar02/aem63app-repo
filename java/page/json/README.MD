# Osgi Configuration API

This is a restful API to access Apache Sling web console osgi configuration in AEM.

  - Allow to access all configurations
  - filter configuration based on pid
  - filter configuration based on factory pid
  - filter configuration based on repository configuration(**sling:OsgiConfig**) type

# Uses
### All Configurations
  URL - http://host:port/bin/api/osgiconfig.json
  
### All repository based Configurations
  URL - http://host:port/bin/api/osgiconfig.json?repo=true
  
### All non repository based Configurations
  URL - http://host:port/bin/api/osgiconfig.json?repo=false
  
### Filtered Configurations (Url + Parameters)
URL - http://host:port/bin/api/osgiconfig.json

***Parameters*** 
- **type** - `pid` or `fid`, specify the search for pid or factoryPid
- **q** - pid or factory pid 
- **repo** - `true` or `false`. Optional parameter is used to filter config based on their repository based config status

> Example
> - http://localhost:4504/bin/api/osgiconfig.json?type=pid&q=org.apache.sling.security.impl.ReferrerFilter
> - http://localhost:4504/bin/api/osgiconfig.json?type=fid&q=org.apache.sling.commons.log.LogManager.factory.config
> - http://localhost:4504/bin/api/osgiconfig.json?type=fid&q=org.apache.sling.commons.log.LogManager.factory.config&repo=true

## JSON Output
JSON output contain array of config objects.

json representation of osgi configuration object
```js
{
    "pid": "org.apache.sling.security.impl.ReferrerFilter",
    "changeCount": 1,
    "properties": {
      "filter.methods": [
        "PUT",
        "DELETE"
      ],
      "exclude.agents.regexp": [
        ""
      ],
      "allow.hosts": [
        ""
      ],
      "allow.hosts.regexp": [
        ""
      ],
      "allow.empty": true
    }
  }
```

json representation of osgi factory configuration object
``` js
{
    "pid": "org.apache.sling.commons.log.LogManager.factory.config.ef61ce8d-cf4f-410b-9eb5-b1d629161880",
    "factoryPid": "org.apache.sling.commons.log.LogManager.factory.config",
    "bundleLocation": "slinginstall:<AEM-installation-dir>\\crx-quickstart\\launchpad\\startup\\1\\org.apache.sling.commons.log-5.1.0.jar",
    "changeCount": 1,
    "properties": {
      "org.apache.sling.commons.log.names": [
        "org.apache.sling.scripting.sightly.js.impl.jsapi.ProxyAsyncScriptableFactory"
      ],
      "org.apache.sling.commons.log.level": "ERROR",
      "org.apache.sling.commons.log.pattern": "{0,date,dd.MM.yyyy HH:mm:ss.SSS} *{4}* [{2}] {3} {5}",
      "org.apache.sling.commons.log.file": "logs/error.log"
    }
  }
```

json representation of osgi factory configuration object for repository based config
``` js
{
    "pid": "org.apache.sling.commons.log.LogManager.factory.config.2fa52a0a-6347-4556-b78f-f0effdd88a61",
    "factoryPid": "org.apache.sling.commons.log.LogManager.factory.config",
    "bundleLocation": "slinginstall:<AEM-installation-dir>\\crx-quickstart\\launchpad\\startup\\1\\org.apache.sling.commons.log-5.1.0.jar",
    "changeCount": 1,
    "properties": {
      "org.apache.sling.commons.log.names": [
        "com.acc.aem64"
      ],
      "org.apache.sling.commons.log.level": "debug",
      "org.apache.sling.commons.log.pattern": "{0,date,yyyy-MM-dd HH:mm:ss.SSS} {4} [{3}] {5}",
      "org.apache.sling.commons.log.file": "logs/project-AEM64App.log"
    },
    "repositoryPaths": [
      "/apps/AEM64App/config/org.apache.sling.commons.log.LogManager.factory.config-AEM64App"
    ]
  }
```

### Errors
if `type` parameter is missing  
````js
"type parameter is missing"
````

if `q` parameter is missing  
````js
"q parameter is missing"
````

if `type` parameter value is not correct  
````js
"invalid type paramaeter value. It should be 'pid' or 'fid'"
````

> Note : if `repo` parameter value is not either `true` or `false`, then results will not be filtered based on `repo` param


### No results
if no configuration is found  
````js
"No configuration found for this request"
````

# POM Gson dependency
````xml
<dependency>
	<groupId>com.google.code.gson</groupId>
	<artifactId>gson</artifactId>
	<version>2.8.5</version>
</dependency>
````
# OSGI Config Dashboard Page
I used this API to create a osgi config dashboard which display all modified osgi config without login to web console.
You can find the package `aem-tools-osgiconfig-1.zip` at https://github.com/arunpatidar02/osgi-config-api/blob/master/aem-tools-osgiconfig-1.zip and check https://aemlab.blogspot.com/2019/05/aem-osgi-config-dashboard.html to know more about this dashboard page.
> Note : This zip package contains only nodes and UI code but doesn't contain OSGi bunndle(Servlet and other Java class). 

> These java classes can be copied for here and use.

# Limitations
User must have read access at `/apps`, `/conf` and `/libs` in order to get respository based configurations. 
Though this can be handle using service user and can be use in non prod publishers also with subservice session.
check https://github.com/arunpatidar02/aem63app-repo/blob/master/java/SeesionInServletWithSubservice.java to know more about how to get sub service session in Servlet.

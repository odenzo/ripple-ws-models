
[![Build Status](https://travis-ci.com/odenzo/ripple-local-signing.svg?branch=master)](https://travis-ci.com/odenzo/ripple-local-signing)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/64c5333412184e23a22590db35f72181)](https://www.codacy.com
/app/odenzo/ripple-ws-models?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=odenzo/ripple
-ws-models&amp;utm_campaign=Badge_Grade)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

[ ![Download](https://api.bintray.com/packages/odenzooss/maven/ripple-ws-models/images/download.svg?version=0.0.10
) ](https://bintray.com/odenzooss/maven/ripple-ws-models/0.1.0/link)


# rippled-wsmodels

Trimmed down model classes for rippled 1.x WebSocket messages.

The goal of this project is provide an flexible strongly typed set of classes to build messages
to communicate with the Ripple `rippled` following the WebSocket API and parse responses.

See Developer Center at https://xrpl.org/rippled-api.html for more information on documented API.

* Warning: This is really just a testbed, and not useful for production code.

* You may also be interesting in local ripple sign/signFor and the library ripple-binary-codec located at
       https://github.com/odenzo/

# Versions
* 0.1.0 -- Still considered as development status
* Current build on Oracle JDK 8 u221
* Cross Build on Scala 2.12.9 and 2.13.0  
* Using Pre-Release Library Dependencies

- Implemented against Rippled v1.x


# Status

I don't even use this much myself anymore, so treat as a source of reference to build on not a stable thing.

# Unit Testing
```sbt test```

Some basic unit testing is done here, really just dev testing. 

It calls the public Ripple server.
Working on public Ripple server is good for finding more complex cases, currently I don't keep a local 
synced version of public XRP network.




# BUGS/DEFICIENCIES

- The classes for transactions are not well designed for constructing message, some builder things would help.


# TODOs

1. Setup JFrog/JCenter for publishing
2. Transaction submission builder


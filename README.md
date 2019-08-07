# rippled-wsmodels

Trimmed down model classes for rippled 1.x WebSocket messages.

The goal of this project is provide an flexible strongly typed set of classes to build messages
to communicate with the Ripple `rippled` following the WebSocket API and parse responses.

See Developer Center athttps://xrpl.org/rippled-api.html for more information on documented API.

* Warning: This is really just a testbed, and not useful for production code.

* You may also be interesting in local ripple sign/signFor and the library ripple-binary-codec located at
       https://github.com/odenzo/

# Versions
* 0.0.8 -- Still considered as development status
* Current build on Oracle JDK 8 u221
* Cross Build on Scala 2.12.9 and 2.13.0  (and it works!)
* Using Pre-Release Library Dependancies

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


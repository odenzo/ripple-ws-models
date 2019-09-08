
[![Build Status](https://travis-ci.com/odenzo/ripple-local-signing.svg?branch=master)](https://travis-ci.com/odenzo/ripple-local-signing)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/64c5333412184e23a22590db35f72181)](https://www.codacy.com/manual/odenzo/ripple-local-signing?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=odenzo/ripple-local-signing&amp;utm_campaign=Badge_Grade)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

[ ![Download](https://api.bintray.com/packages/odenzooss/maven/ripple-ws-models/images/download.svg?version=0.0.10
) ](https://bintray.com/odenzooss/maven/ripple-ws-models/0.1.0/link)


# rippled-wsmodels

Trimmed down model classes for rippled 1.x WebSocket messages.

The goal of this project is provide an flexible strongly typed set of classes to build messages
to communicate with the Ripple `rippled` following the WebSocket API and parse responses.

See Developer Center at https://xrpl.org/rippled-api.html for more information on documented API.

* Warning: This is really just a testbed, and not useful for production code, however
it is used extensively in  https://github.com/odenzo/ripple-fixture-maker which is a test bed to try
out operations on rippled in a heavily debugged (and sequential) manner.

* You may also be interesting in local ripple sign/signFor and the library ripple-binary-codec located at
       https://github.com/odenzo/

# Design Approach
Well, its a bunch of model objects, as case classes with Circe codecs. I had challenges with using parameterized case
 classs and Circe (and Monocle) so:
 
 1. RippleTx is an unsealed trait for xxTx objects that model all the transactions (e.g. Payment). The common/optional
 values are stored in TxCommon for requests, and CommonTxRs for responses to sign/submit requests.
 
 2. RippleRq/RippleRs are used to model the command request and response core fields, with CommonCmdRq and
  CommonCmdRs holding common values.

3. All xxxTx objects have encoders/decoders.

4. Working towards all RippleRq/RippleRs objects having encoders and decoders -- all the RippleRq have encoders and
 all the RippleRs have decoders at the minimum.
 
This allows most derivation of codecs to be done semi-automatically, and have just a few cases where the common rq/rs
 are merged into Transaction or Command specific objects.
 
 There are helpers, which I am trying to trim down.   https://github.com/odenzo/ripple-fixture-maker has some example
  usages.

# Versions
* 0.2.0 -- Still considered as development status, but transactions and most commands usable.
* Current build on Oracle JDK 8 u221
* Cross Build on Scala 2.12.9 and 2.13.0  
* Using Pre-Release Library Dependencies

- Implemented against Rippled v1.x


# Status

This was a decrepit collection of helpers, but its getting fairly well cleaned up.


# Unit Testing
```sbt test```

Some basic unit testing is done here, really just dev testing. 



# BUGS/DEFICIENCIES

- The classes for transactions are not well designed for constructing message, some builder things would help.


# TODOs

1. More testing
2. Fleshing out ledger state and ledger objects.
3. AccountSet filling out.


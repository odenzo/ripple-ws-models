# Overview of rippled-wsmodels from Users Perspective

Ripple servers natively support a websocket communication model. 
There are four primary types of interaction supported:
1. Commands -- These are issued and take effect or fail immediately.
2. Admin Commands -- Commands that require server authentication, typically must own the rippled server
3. Subscriptions -- These are queries that request the rippled server to respond with a long running stream of 
information messages, e.g. subscribing to notifications/events whenever a ledger is committed
4. Transactions -- These are transactions because they are multistep actions are require signing/authentication.

See: https://developers.ripple.com/ for general documentation.



This systems has a lot of model objects to model the wireprotocal of messages sent over the websocket, in addition to
 some utility routines.
 
** This is simply a series of helper objects, that is slowly getting cleaned up and more opinionated**

When things can be done multiple ways, I am choosing my favorite way to eliminate complexity.
This mainly applies to building request messages. 

 
 ## RippleCommands
 This trait, and objects that extend it define the model objects for a command request and response.
 `RippleCommands.scala` groups these into object spaces corresponding to the documentation on the Ripple website.

https://developers.ripple.com/public-rippled-methods.html
 
 Each has the Signature:
 scala`
  implicit object AccountChannelsCmd extends RippleCommand[AccountChannelsRq, AccountChannelsRs] {
         val encoder: Encoder[AccountChannelsRq] = Encoder[AccountChannelsRq]
         val decoder: Decoder[AccountChannelsRs] = Decoder[AccountChannelsRs]
       }
       `
       
       
See RippleCommand base trait for some common API.
These are implicit objects because.... well I forget now to tell the truth. Some typeclass experiment.
No real need now of course.       


# Notes on Currency

- CurrencyAmount is either native and XRP or a FiatAmount
- XRP values in Drops, note that maximum drops bigger than Long.MAX
- FiatAmount consists of an amount in a Script
- A Script in Ripple consists of a Currency (e.g. USD, FOO) an Issuer of the Currency (RippleAddress)


# Notes on Accounts

- Account is AccountAddr or AccountAlias, account alias is something like ~odenzo . Deprecated and AccountAddr 
 or DTAccountAddr should be used in requets. 
- DTAccountAddr models an account and a destination tag. Not that all desintation tags for an account are
  aggregated into one account on the ledger in practice.
  

- There are some helper classes that wrap these with secret keys and labels etc (e.g. RipppleAccountRO) but these 
should be considered extras and fill be factored out. Currently these are in in com.odenzo.rippl.models.support
.SpecialAddresse, which also has some standard stuff that is the same in each ledger (e.g. Genesis, AccountZero and 
AccountOne)

- ** Moving to make all the requests support Account instead of AccountAddr as needed.** I forget why removed, 
possibly because I think account aliases were deprecated?  


 

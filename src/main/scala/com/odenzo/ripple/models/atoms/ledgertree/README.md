# Ledger Atoms

This is an attempt, ignore TxRs entries and TxNode for now, to enumerate the Ledger atoms
per documentation at: https://ripple.com/build/ledger-format/

It leverages atoms from elsewhere too.

### Ledger Nodes returned by Account Tx Inquiries

Ledger nodes are pretty complex, and also have overlap but not equivalence depending on the context.

There are two primary source of ledger info, account_tx  and the commands grouped under ledger.
On submission some partial ledger like information is also returned.
account_tx is of primary interest to me, and I ended up confusing myself so re-writing to here.

## Technical Approach

Case classes are handy, but can't deal with inheretence. Option is to use Shapeless generic styles, dynamic style like
hashmap or use regular classes.

Since these are primarly READ-ONLY and only decoded I think a shapeless approach would be best.
But, started with case classes so continue that major duplication approach to start with.

We start the tree here with the CONTENTS of transactions's meta nodes.


## Structure:

account_tx meta element contains a list of effected ledger nodes.

These nodes are either modified nodes or created nodes.
 
This is modeled as:
- AffectedNodes is a List[LedgerNode]
- ModifiedNode is a type of LedgerNode
- CreatedNode is a type of LedgerNode
 
 Each LedgerNode contains information related to the type of ledger entry that was created/modified:
  
  1. AccountRoot type ledger entry
  2. RippleState type ledger entry
  3. DirectoryNode 


It would seem that LedgerEntryType and LedgerIndex are the only consistent fields present in all Modified and Created
 Nodes.
 



## Reference
https://github.com/ripple/rippled/blob/master/src/ripple/protocol/impl/LedgerFormats.cpp

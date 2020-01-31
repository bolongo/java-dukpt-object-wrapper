# Java DUKPT Object Wrapper

A wrapper for the https://github.com/bolongo/java-dukpt library, which is a fork from the original
https://github.com/SoftwareVerde/java-dukpt from Software Verde. This library aims to simplify the convertion of keys
from their multiple representations (as Bitsets, Byte Arrays, Hexadecimal Strings)

## v. 1.0.0
The first release of the library

## v. 1.0.1
Access correction

Public class access was missing from BaseCode, BDK, IPEK and KSN classes

## v. 1.0.2
Access correction from inner classes

Public class access was missing from KSN.TransactionCounterOverflowException and KSN.TransactionCounterUnderflowException
classes
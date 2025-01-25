# Quarantine-Style
Program that count frequency of the word in a given text, following the Quarantine-Style
These are the constraints for the Quarantine style:
- Core program functions have no side effects of any kind, including IO.
- All IO actions must be contained in computation sequences that are clearly separated from the pure functions.
- All sequences that have IO must be called from the main program.\

The aim of this code was to exercise on Monad, a design pattern used to handle functions in a consistent and composable way.
Monads has two features:
- UNIT -> is used to wrap values into a monad
- BIND -> is used to chains monadic operation by taking a function and applying it to the contained value

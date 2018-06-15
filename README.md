# Diffy
Uses Android's DiffUtil Update callbacks to determine what has changed from two lists.

Backed by Eugene W. Myers's difference algorithm to calculate the minimal number of updates
 * to convert one list into another. Myers's algorithm does not handle items that are moved so
 * DiffUtil runs a second pass on the result to detect items that were moved.

Very basic. To verify conceptual proof only.

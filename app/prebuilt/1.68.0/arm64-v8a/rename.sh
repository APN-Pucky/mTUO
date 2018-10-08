#!/bin/bash
for i in *; do
	mv "$i" "`echo $i | sed "s/\([a-zA-Z0-9_]*\)-.*/\1\.a/"`";
done

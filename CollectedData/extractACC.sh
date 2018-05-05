cat $1 | grep "^ACC:" | tr -dc "\-{0,1}[0-9]\.[0-9]* "

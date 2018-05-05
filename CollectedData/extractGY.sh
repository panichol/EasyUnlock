cat $1 | grep "^GY:" | tr -dc "\-?[0-9]\.[0-9]* "

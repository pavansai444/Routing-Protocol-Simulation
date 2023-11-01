BEGIN {
dcount = 0;
rcount = 0;
}
{
event = $1;
time=$2;
if(event == "d")
{
dcount++;

}
if(event == "r")
{
rcount++;
}

}
END {
printf("The no.of packets dropped  : %d\n ",dcount);
printf("The no.of packets recieved : %d\n ",rcount);
printf("Packet Delivery Ratio : %.2f%%\n", (rcount*100)/(rcount+dcount));

}
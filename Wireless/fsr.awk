BEGIN   {
            agt_count=0;
            rtr_count=0;
            dropped=0;
            routing_packets=0;
        }
{
    if ($1 == "s" && $4 == "AGT") {
        agt_count++;
    } else if ($1 == "s" && $4 == "RTR") {
        rtr_count++;
    }   if (($1 == "s" || $1 == "f" || $1="r") && $4 == "RTR" && ($7 =="AODV" ||$7 =="AOMDV")) routing_packets++;
}
END {
    printf("AGT %d\n", agt_count);
    printf("RTR %d\n", rtr_count);
    printf("Routing Packets = %.2f\n",routing_packets);
}
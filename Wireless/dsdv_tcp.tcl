# Set the wireless network parameters
set val(chan) Channel/WirelessChannel;
set val(prop) Propagation/TwoRayGround;
set val(netif) Phy/WirelessPhy;
set val(mac) Mac/802_11;
set val(ifq) Queue/DropTail/PriQueue;
set val(ll) LL;
set val(ant) Antenna/OmniAntenna;
set val(ifqlen) 50;
set val(rp) DSDV;
set val(nn) 11;
set val(x) 500;
set val(y) 400;
set val(stop) 3;

# Energy model settings
set val(energymodel) EnergyModel;
set val(initialenergy) 1000;

# Create a new NS (Network Simulator) instance
set ns [new Simulator]

# Create trace files
set tf [open ns_dsdv_tcp.tr w]
$ns trace-all $tf

set nf [open ns_dsdv_tcp.nam w]
$ns namtrace-all-wireless $nf $val(x) $val(y)

# Create a network topology
set topo [new Topography]
$topo load_flatgrid $val(x) $val(y)

# Create GOD (Global Positioning System for Ad Hoc Networks)
create-god $val(nn)

# Create a wireless channel
set chan_1_ [new $val(chan)]

# Node configuration
$ns node-config -adhocRouting $val(rp) \
	-llType $val(ll) \
	-macType $val(mac) \
	-ifqType $val(ifq) \
	-ifqLen $val(ifqlen) \
	-antType $val(ant) \
	-propType $val(prop) \
	-phyType $val(netif) \
	-channel $chan_1_ \
	-topoInstance $topo \
	-agentTrace ON \
	-routerTrace ON \
	-macTrace OFF \
	-movementTrace ON \
	-energyModel $val(energymodel) \
	-initialEnergy $val(initialenergy) \
	-rxPower 0.4 \
	-txPower 1.0 \
	-idlePower 0.6 \
	-sleepPower 0.1 \
	-transitionPower 0.4 \
	-transitionTime 0.1

# Create nodes and set their initial positions
for {set i 0} {$i < $val(nn)} {incr i} {
	set node_($i) [$ns node]
	$node_($i) set X_ [expr 10 + round(rand() * 480)]
	$node_($i) set Y_ [expr 10 + round(rand() * 380)]
	$node_($i) set Z_ 0.0
}
for {set i 0} {$i < $val(nn)} {incr i} {
	$ns at [ expr 0.2+round(rand()) ] "$node_($i) setdest [ expr 10+round(rand()*480) ] [expr 10+round(rand()*380) ] [expr 60+round(rand()*30) ]"
}
# Set the destination for TCP traffic
set tcp_dest $node_(2)

# Create a TCP sender agent
set tcp_sender [new Agent/TCP]
$ns attach-agent $node_(5) $tcp_sender

# Create a TCP receiver agent
set tcp_receiver [new Agent/TCPSink]
$ns attach-agent $node_(2) $tcp_receiver

# Create a TCP application (FTP) to generate TCP traffic
set ftp [new Application/FTP]
$ftp attach-agent $tcp_sender
# $ftp set type_ FTP
$ftp set maxpkts_ 10000  # Number of packets
$ftp set rate_ 1mb   # Data rate
$ftp set packetSize_ 10000  # Packet size

# Connect the TCP agents
$ns connect $tcp_sender $tcp_receiver

# Start the FTP application
$ns at 0.5 "$ftp start"

for {set i 0} {$i < $val(nn)} {incr i} {
        $ns initial_node_pos $node_($i) 30
}
# Schedule node resets
for {set i 0} {$i < $val(nn)} {incr i} {
	$ns at $val(stop) "$node_($i) reset"
}

# Finish and run the simulation
$ns at $val(stop) "finish"
$ns at 3.1 "puts \"End simulation\"; $ns halt"

# Procedure to finish the simulation
proc finish {} {
	global ns tf nf
	$ns flush-trace
	close $tf
	close $nf
	exec nam ns_dsdv_tcp.nam &
	exit 0
}

puts "CBR packet size = [$ftp set packetSize_]"
puts "CBR interval = [$ftp set rate_]"

$ns run

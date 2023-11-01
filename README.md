# Routing Protocol Simulation
 implemented various routing protocols in java ans simulated using ns2
Wireless Protocols: 

We conducted simulations using the ns2 simulator to evaluate different ad hoc protocols in a dynamic and resource-constrained environment, where the number of nodes varied, such as in a 50-node network. During these simulations, we analysed trace files to gather valuable metrics, including throughput, packet delivery ratio, end-to-end delay, and the counts of dropped, received, and sent packets.And also, we coded tcl ,awk files to get the results. 

Wireless Protocols that we have simulated: 

Pro active: 

DSDV Protocol – contributed by Balaga Pavan Sai 

FESR Protocol - contributed by Challa Vishwanath 

Reactive: 

AODV Protocol - contributed by Nagasamudram Karthik 

DSR Protocol - contributed by Taviti Venkata Manikanta 

 Wired Protocols: 

 

We have implemented Java code to execute the algorithms associated with both distance vector and link state protocols. This code enables us to derive the routing tables for each router. Subsequently, we generate a TCL (Tool Command Language) script, which serves as the basis for simulating these protocols in ns2 simulator. Additionally, we employ an AWK script (a scripting language) to parse the trace file, extracting critical metrics such as throughput, packet delivery ratio, end-to-end delay, as well as the quantities of dropped, received, and transmitted packets. In our analysis, we also utilize the X Graph tool to visually represent the throughput data through graph plotting. 

      Wired Protocols that we have implemented: 

Link State routing -contributed by Balaga Pavan Sai and challa vishwanath) 

Distance Vector routing- contributed by Nagasamudram Karthik and Taviti Venkata Manikanta 

Upgradation to wired Network Protocols: 

we have employed Java programming to deploy the advanced OSPF (Open Shortest Path First) protocol, which represents a significant leap beyond the conventional link state protocol. OSPF's dynamic updates and real-time routing table management for each router make it a powerful tool for optimizing network performance. By integrating OSPF into our simulations, we have enabled the NS2 simulator to dynamically respond to network changes, showcasing OSPF's capacity for highly responsive and efficient routing in complex network environments. 

Tcl script generation in code for dynamic changes in links- contributed by B Pavan Sai 

Modifications in java code for including extra features like dynamic changes contributed by Taviti Venkata Manikanta 

Developed scripting language(awk) for analysing trace file contributed by Nagasamudram Karthik 

Plotted Various metrics using X graph tool- contributed by Challa ishwanath 

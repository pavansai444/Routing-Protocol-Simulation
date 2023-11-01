

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

//Node for the graph
class Node implements Comparable<Node>{

    int val, cost;

    Node(int val, int cost){
        this.val = val; this.cost = cost;
    }
    
    public int compareTo(Node x){
        return Integer.compare(this.cost, x.cost);
    }
}

//Graph
class Graph{
    
    private ArrayList<ArrayList<Node>> adj;
    private int numVertices, numEdges;
    
    Graph(int numVertices, int numEdges){
    
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        
        adj = new ArrayList<ArrayList<Node>>(numVertices+1);
            
        for(int i=0; i<numVertices; i++)
            adj.add(new ArrayList<Node>(numVertices+1));
    }

    //getter methods
    ArrayList<ArrayList<Node>> getAdjacency(){
        return adj;
    }

    int getNumVertices(){
        return numVertices;
    }
    
    int getNumEdges(){
        return numEdges;
    }

    //adding edge to Graph
    void addEdge(int source, int destination, int cost){
        if(isValidEdge( source, destination, cost)){
            addEdgeToGraph(source, destination, cost);
            System.out.println("Link added");
        }
        else{
            System.out.println("Link not added");
        }
    }

    private void addEdgeToGraph(int source, int destination, int cost){
        
        adj.get(source).add(new Node(destination, cost));
        adj.get(destination).add(new Node(source, cost));
    }
    
    // validating edges
    private boolean isValidEdge(int source, int destination, int cost){
        
        if(cost < 0){
            System.out.println("Negative cost now allowed");
            return false;
        }
        
        // invalid vertex
        if(0 > source || source >= numVertices || 0 > destination || destination >=  numVertices){
            System.out.println("Invalid vertices for edges - Invalid Edges");
            return false;
        }
        
        // self loops not allowed
        if(source == destination){
            System.out.println("Self loops not allowed");
            return false;
        }
        
        return true;
        
    }
    //if graph is empty
    boolean isEmpty(){
        int j = 1;
        for(int i=0; i<numVertices; i++){
            if(adj.get(i).size()>0){
                j++;
            }
        }
        
        if(j == numVertices)
            return true;
        else
            return false;
    }
}

// Dijsktra
class Dijsktra{

    private int[] dist,nextHop;
    private PriorityQueue<Node> pq;
    private ArrayList<ArrayList<Node>> adj;
    
    
    void dijsktraAlgorithm(int start, Graph graph){
        
        runDijsktraAlgorithm(start, graph);
    }

    private void runDijsktraAlgorithm(int start, Graph graph){
        
        int numVertices = graph.getNumVertices();
        
        adj = graph.getAdjacency();
        
        dist = new int[numVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        
        nextHop = new int[numVertices];
        Arrays.fill(nextHop, -1);
        
        dist[start] = 0;
        
        pq = new PriorityQueue<Node>();
        pq.add(new Node(start, 0));
        
        while(pq.size() > 0){
            
            Node curr = pq.poll();
            int currN = curr.val;
            
            Iterator<Node> it = adj.get(currN).iterator();
            
            while(it.hasNext()){
            
                Node temp = it.next();
            
                if(dist[temp.val] > dist[currN] + temp.cost){
            
                    pq.add(new Node(temp.val, dist[currN]+temp.cost));
                    dist[temp.val] = dist[currN] + temp.cost;
                    updateNextHop(start, currN, temp);
                    
                }
            }
        }
    }    
    
    private void updateNextHop(int start, int currN, Node temp){
       
        if(start != currN ){
            nextHop[temp.val] = currN;
            
            if(nextHop[temp.val] != -1){
                
                int tempNode = nextHop[nextHop[temp.val]];    
                
                while(tempNode != -1){
                    nextHop[temp.val] = nextHop[nextHop[temp.val]];
                    tempNode = nextHop[nextHop[temp.val]];
                }    
            }
        }
       
    }
    
    void printShortestPath(int start){
        
        for(int i=0; i<dist.length; i++){
            if(i!=start){
                System.out.println(i+"\t"+((dist[i] == Integer.MAX_VALUE)?"No link":dist[i]+"\t"+((nextHop[i] == -1)?"- ":nextHop[i])+" ")+"\t");
            }
        }
    }
    
    //getter methods
    int[] getNextHop(){
        return nextHop;
    }
    
    int[] getDist(){
        return dist;
    }
}

//Routing Table
class RoutingTable{
    
    private int router,numRouters;
    private int[] dist, nextHop;
    
    RoutingTable(int router, int numRouters){
        
        this.router = router;
        this.numRouters = numRouters;
        dist = new int[numRouters];
        Arrays.fill(dist, Integer.MAX_VALUE);
        
        nextHop = new int[numRouters];
        Arrays.fill(nextHop, -1);
        
    }
    
    // setter methods
    void setDist(int[] dist){
        this.dist = dist;
    }
    
    void setNextHop(int[] nextHop){
        this.nextHop = nextHop; 
    }
    
    // getter methods
    int[] getDist(){
        return dist;
    }
    
    int[] getNextHop(){
        return nextHop;
    }
    
    // print routing table
    void printRoutingTable(){
        for(int i = 0; i < numRouters; i++){
            if(i!=router){
                System.out.println(i+"\t"+((dist[i] == Integer.MAX_VALUE)?"No link":dist[i]+"\t"+((nextHop[i] == -1)?"- ":nextHop[i])+" ")+"\t");
            }
        }
    }
}

// NetworkTopology class
class NetworkTopology{
    
    Graph network;
    int numRouters;
    int numRouterLinks;
    Dijsktra d[];
    RoutingTable routingTable[];
    
    NetworkTopology(int numRouters, int numRouterLinks){
        
        this.numRouters = numRouters;
        this.numRouterLinks = numRouterLinks;
        
        network = new Graph(numRouters, numRouterLinks);
        routingTable = new RoutingTable[numRouters];
        
        d = new Dijsktra[numRouters];
        
        for(int i = 0; i < numRouters; i++){
            d[i] = new Dijsktra();
        }
    }
    
    // adding router link to network
    void addLink(int source, int destination, int cost){
        
        network.addEdge(source,destination,cost);
    }

    // building routing table for every router
    void buildRoutingTables(){
        
        for(int i = 0; i < numRouters; i++){
            d[i].dijsktraAlgorithm(i, network);
            routingTable[i] = new RoutingTable(i, numRouters);
            routingTable[i].setDist(d[i].getDist());
            routingTable[i].setNextHop(d[i].getNextHop());
        }
    }
    
    // printing routing tables for all routers
    void printRoutingTables(){
        
        for(int i = 0; i < numRouters; i++){
            System.out.println("\n--------------------------------");
            System.out.println("Routing Table for Router "+i);
            System.out.println("--------------------------------");
            System.out.println("Node\tCost\tNext Hop");
            routingTable[i].printRoutingTable();
            System.out.println("--------------------------------");
        }
    }
    
    // simulating router behaviour to find the path
    void simulateRouter(int src, int dest){
        
        if(src >=0 && src<numRouters && dest>=0 && dest<numRouters  ){
            simulateRouterBehaviour(src, dest);
        }else{
            System.out.println("Invalid source and/or destination");
        }    
    }
    
    private void simulateRouterBehaviour(int source, int destination){
        
        int dist[] = routingTable[source].getDist();
        int nextHop[] = routingTable[source].getNextHop();
        
        if(dist[destination] == Integer.MAX_VALUE){
            System.out.println("No link exists");
            return;
        }
        
        if(source == destination){
            System.out.println("Self loop");    
        }
        else{
            System.out.println("Link exists");
        }
        
        int next = nextHop[destination]; 
        
        if(next == -1){
            System.out.println(source +" - "+destination);
        }
        else{
            System.out.print(source+" - ");
            while(next !=-1){
                System.out.print(next+" - ");
                nextHop = routingTable[next].getNextHop();
                next = nextHop[destination];
                
            }
            System.out.print(destination);
        }
    }
    
}

public class linkstate {

    public static void main(String[] args) {
        
        NetworkTopology networkTopology;
        int source, destination, cost;
        int src,dest,again,repeat;
        boolean valid = true;
        File file = new File("simulation_ls.tcl");
        try(FileWriter fw = new FileWriter(file,StandardCharsets.UTF_8,false)){
        fw.write("set ns [new Simulator]\n");
        fw.write("$ns rtproto Session\n");
        fw.write("\n");
        fw.write("set tf [open out_ls.tr w]\n$ns trace-all $tf\nset nf [open out_ls.nam w]\n$ns namtrace-all $nf");
        fw.write("\n");    
        fw.write("set xf [open out_ls.xg w]\n");
        Scanner sc = new Scanner(System.in);
        
        // do
        {
            System.out.println("\nLINK STATE ROUTING\n");
            
            System.out.print("Enter the number of routers in your network: ");    
            int numRouters = sc.nextInt();
            fw.write("\nset val(nn) "+numRouters+"\n");
            fw.write("for {set i 0} {$i < $val(nn)} {incr i} {\n");
            fw.write("  set node($i) [$ns node]\n}\n");
            System.out.print("Enter the number of router links in your network: ");
            int numLinks = sc.nextInt();
            
            valid = true;
            
            // validating input
            if(numRouters < 0){
                System.out.println("\nInvalid number of routers\n");
                valid = false;
            }
            else if(numRouters == 0){
                System.out.println("\nNo routers- Therefore, empty network\n");
                valid = false;
            }
            else if(numLinks < 0){
                System.out.println("\nInvalid number of links\n");
                valid = false;
            }
            else if(numLinks == 0){
                System.out.println("\n0 links - Therefore, empty network\n");
                valid = false;
            }
            
            // forming network of routers(graph)
            if(valid == true){
                System.out.println("\n--------------------------------");
                System.out.print("Your routers are: ");
                for(int i =0; i < numRouters; i++){
                    System.out.print(i+" ");
                }
                System.out.println("\n--------------------------------");
                networkTopology = new NetworkTopology(numRouters,numLinks);
                
                int i = 0;
                System.out.print("\nEnter inputs for your router links: ");    
                while(numLinks-- > 0){
                    
                    System.out.println("\nRouter Link "+ ++i);
                    
                    System.out.print("Enter source router of the link: ");
                    source = sc.nextInt();
                    System.out.print("Enter destination router of the link: ");
                    destination = sc.nextInt();
                    System.out.print("Enter cost of the link: ");
                    cost = sc.nextInt();
                    fw.write("$ns duplex-link $node("+source+") $node("+destination+") 100Mb 10ms DropTail\n");
                    fw.write("$ns cost $node("+source+") $node("+destination+") "+cost+"\n");
                    fw.write("$ns cost $node("+destination+") $node("+source+") "+cost+"\n");
                    networkTopology.addLink(source,destination,cost);
                }
                
                // building routing tables
                networkTopology.buildRoutingTables();
                networkTopology.printRoutingTables();
                
                
                // SIMULATING router behaviour
                // do
                {
                    System.out.println("\nSIMULATING ROUTER:");
                    System.out.println("To simulate the router behaviour:");
                    System.out.print("Enter source router       : ");
                    src = sc.nextInt();
                    System.out.print("Enter destination router  : ");
                    dest = sc.nextInt();
                    System.out.println("--------------------------------");
                    if(src>=0 && src<numRouters && dest>=0 && dest<numRouters  ){
                        fw.write("\nset tcp2 [new Agent/TCP]\n");
                        fw.write("$ns attach-agent $node("+src+") $tcp2\n");
                        fw.write("set sink2 [new Agent/TCPSink]\n");
                        fw.write("$ns attach-agent $node("+dest+") $sink2\n");
                        fw.write("$ns connect $tcp2 $sink2\n\n");

                        fw.write("set traffic_ftp2 [new Application/FTP]\n");
                        fw.write("$traffic_ftp2 attach-agent $tcp2\n");

                        System.out.println("The router will follow the path: ");
                        networkTopology.simulateRouter(src,dest);
                        fw.write("\nproc finish {} {\n");
                        fw.write("global ns nf tf xf\n$ns flush-trace\nclose $nf\nclose $tf\nexec nam out_ls.nam &\nexec awk -f stats.awk out_ls.tr &\nexit 0\n}\n");
                        
                        fw.write("proc record {} {\n");
                        fw.write("global sink2 xf\nset ns [Simulator instance]\n");
                        fw.write("set time 0.5\nset bw [$sink2 set bytes_]\nset now [$ns now]\n");
                        fw.write("puts $xf \"$now [expr $bw/$time*8/1000000]\"\n");
                        fw.write("$sink2 set bytes_ 0\n$ns at [expr $now+$time] \"record\"\n}\n\n");


                        fw.write("$ns at 1.0 \"$node("+src+") label sender\"\n");
                        fw.write("$ns at 5.0 \"$node("+src+") label \\\"\\\"\"\n");
                        fw.write("$node("+src+") color green\n");

                        fw.write("$ns at 1.0 \"$node("+dest+") label reciever\"\n");
                        fw.write("$ns at 5.0 \"$node("+dest+") label \\\"\\\"\"\n");
                        fw.write("$node("+dest+") color blue\n");

                        fw.write("for {set i 0} {$i < $val(nn) } { incr i } {\n");
                        fw.write("  $ns at 6.0 \"$node($i) reset\";\n}\n");
                        
                        fw.write("$ns at 0.0 \"record\"");
                        fw.write("\n$ns at 1.0 \"$traffic_ftp2 start\"\n$ns at 3.0 \"$traffic_ftp2 stop\"\n");
                        fw.write("$ns at 5.0 \"finish\"\n");
                        fw.write("$ns run\n");
                    }
                    else{
                        System.out.println("Invalid Source and/or destination");
                    }  
                    // System.out.println("\n--------------------------------");
                    // System.out.println("Do you want to simulate again? Enter 1 if yes: or 0 if No: ");
                    // again = sc.nextInt();
                    // System.out.println("\n--------------------------------");
                }
                // while(again==1);
            }
            // System.out.println("\n--------------------------------");
            // System.out.print("Do you want to build a new network? Enter 1 if yes: or 0 if No: ");
            // repeat = sc.nextInt();
            // System.out.println("------------------------------------");
        }
        // while(repeat ==1);    
                
        fw.close();
        System.out.println("\nTHE END OF SIMULATION");
        System.out.println("---------------------------------");
        }catch(IOException e){
            e.printStackTrace();
        }
        // NS Simulation..
        try {
            // ProcessBuilder compileProcess= new ProcessBuilder("ns","simulation.tcl");
            // Process compile = compileProcess.start();

            // int compileExitCode = compile.waitFor();
            // if(compileExitCode == 0){
                // Execute
                ProcessBuilder executeProcess = new ProcessBuilder("ns","simulation_ls.tcl");
                Process execute = executeProcess.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(execute.getInputStream()));
                String line;
                while( (line= reader.readLine())!=null){
                    System.out.println(line);
                }

                int exitCode = execute.waitFor();
                // System.out.println("Exit Code: "+exitCode);
            // }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }

}
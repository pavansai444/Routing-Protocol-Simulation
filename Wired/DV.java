import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

// node
class Node implements Comparable<Node>{

    int val, cost;

    Node(int val, int cost){
        this.val = val; this.cost = cost;
    }
    
    public int compareTo(Node x){
        return Integer.compare(this.cost, x.cost);
    }
}

// graph
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

class bellman{

     int[] dist,nextHop;
     PriorityQueue<Node> pq;
     ArrayList<ArrayList<Node>> adj;
     int numVertices;
    
    void intialize(int nod, Graph graph){
         numVertices = graph.getNumVertices();
        
        adj = graph.getAdjacency();
        
        dist = new int[numVertices];
        
        Arrays.fill(dist, Integer.MAX_VALUE);
        nextHop = new int[numVertices];
        Arrays.fill(nextHop, -1);
        
        dist[nod]=0;
        Iterator<Node> it = adj.get(nod).iterator();
        while(it.hasNext()){
            
                Node temp = it.next();
                dist[temp.val] = temp.cost;
                nextHop[temp.val] = temp.val;
        }
    }

     void runBellmanAlgorithm(int nod,int val,int [] neig,int cost){
        
       
         for(int i = 0;i<numVertices;i++){
            if(i!=nod){
                if(neig[i]!=Integer.MAX_VALUE){
                       if(dist[i]>(neig[i]+cost)){
                        dist[i] = neig[i]+cost;
                        nextHop[i] = val;
                       }
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
                                System.out.println(i+"\t"+((dist[i] == Integer.MAX_VALUE)?"INF":dist[i]+"\t"+((nextHop[i] == -1)?"- ":nextHop[i])+" ")+"\t");

            }
        }
    }
}

// NetworkTopology class
class NetworkTopology{
    
    Graph network;
    int numRouters;
    int numRouterLinks;
    bellman d[];
    RoutingTable routingTable[];
    int[] neidist;
    NetworkTopology(int numRouters, int numRouterLinks){
        
        this.numRouters = numRouters;
        this.numRouterLinks = numRouterLinks;
        
        network = new Graph(numRouters, numRouterLinks);
        routingTable = new RoutingTable[numRouters];
        
        d = new bellman[numRouters];
        neidist = new int[numRouters];
        for(int i = 0; i < numRouters; i++){
            d[i] = new bellman();
        }
    }
    
    // adding router link to network
    void addLink(int source, int destination, int cost){
        
        network.addEdge(source,destination,cost);
    }

    // building routing table for every router
    void buildRoutingTables(){
        ArrayList<ArrayList<Node>> adj = network.getAdjacency();
        for(int i = 0; i < numRouters; i++){
            d[i].intialize(i, network);
            routingTable[i] = new RoutingTable(i, numRouters);
            routingTable[i].setDist(d[i].getDist());
            routingTable[i].setNextHop(d[i].getNextHop());
        }
         System.out.println("intial Routing tables");
        printRoutingTables();
        for(int j = 1;j<numRouters;j++){
            for(int i =0;i<numRouters;i++){
    Iterator<Node> it = adj.get(i).iterator();
    while(it.hasNext()){
         Node temp = it.next();
d[i].runBellmanAlgorithm(i,temp.val,d[temp.val].getDist(),temp.cost);
    }
routingTable[i] = new RoutingTable(i, numRouters);
 routingTable[i].setDist(d[i].getDist());
 routingTable[i].setNextHop(d[i].getNextHop());
            }
            System.out.println();
             System.out.println();
             System.out.println("Routing tables after "+j+" iterations");
              System.out.println();
               System.out.println();
             printRoutingTables();

        }
    }
    
    // printing routing tables for all routers
    void printRoutingTables(){
        
        for(int i = 0; i < numRouters; i++){
            System.out.println("\n--------------------------------");
            System.out.println("Routing Table for Router "+i);
            System.out.println("--------------------------------");
            System.out.println("Node\tCost\tNextHop");
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
    
    void simulateRouterBehaviour(int source, int destination){
        
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
            while(next != destination){
                System.out.print(next+" - ");
                nextHop = routingTable[next].getNextHop();
                next = nextHop[destination];
                
            }
            System.out.print(destination);
        }
    }
    
}

public class DV {

    public static void main(String[] args) {
        
        NetworkTopology networkTopology;
        int source, destination, cost;
        int src,dest,again,repeat;
        boolean valid = true;
        File file = new File("simulation_dv.tcl");
        try(FileWriter fw = new FileWriter(file,StandardCharsets.UTF_8,false)){
        fw.write("set ns [new Simulator]\n");
        fw.write("$ns rtproto Session\n");
        fw.write("\n");
        fw.write("set tf [open out_dv.tr w]\n$ns trace-all $tf\nset nf [open out_dv.nam w]\n$ns namtrace-all $nf");
        fw.write("\n");
        fw.write("set xf [open out_dv.xg w]");
        Scanner sc = new Scanner(System.in);
        
    
            System.out.println("\nDistance Vector ROUTING\n");
            
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

                
                
                // SIMULATING router behaviour
               
                    System.out.println("\nSIMULATING ROUTER:");
                    System.out.println("To simulate the router behaviour:");
                    System.out.print("Enter source router: ");
                    src = sc.nextInt();
                    System.out.print("Enter destination router: ");
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
                        fw.write("global ns nf tf xf\n$ns flush-trace\nclose $nf\nclose $tf\nclose $xf\nexec nam out_dv.nam &\nexec awk -f stats.awk out_dv.tr &\n  exit 0\n}\n");
                        
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
                    System.out.println("\n--------------------------------");
               
            }
            // System.out.println("\n--------------------------------");
            // System.out.print("Do you want to build a new network? Enter 1 if yes: ");
            // repeat = sc.nextInt();
            // System.out.println("------------------------------------");
          
                
        fw.close();
        System.out.println("\nTHANK YOU");
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
            ProcessBuilder executeProcess = new ProcessBuilder("ns","simulation_dv.tcl");
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
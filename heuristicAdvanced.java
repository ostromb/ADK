import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class heuristicAdvanced {
    //public static ArrayList<Role> roles;
    public static graph castGraph;
    static class Role {
        public boolean isDiva;
        public int actor;
        public boolean isSet;
        public int[] candidates;
        public int rolenr;
        public boolean canBeDiva;
        public Role(int[] candidates,int rolenr) {
            this.candidates = candidates;
            this.isSet = false;
            this.rolenr = rolenr;
        }
        public void setActor(int actor,boolean isDiva){
            this.actor = actor;
            this.isDiva = isDiva;
        }
        public int getActor(){
            return actor;
        }
        public void setisSet(){
            this.isSet = true;
        }
        public boolean getisSet() {
            return isSet;
        }
        public boolean getisDiva() {
            return isDiva;
        }
        public void setcanBeDiva(boolean canBediva){
            this.canBeDiva = canBediva;
        }
        public boolean getcanBeDiva() {
            return canBeDiva;
        }
    }
    
    static class graph {
        int numVertices;
        public Role[][] neigbors;
        public graph(int numVertices){
            this.numVertices = numVertices;
            this.neigbors = new Role[numVertices+1][numVertices+1];
        }
        public void setNeighbors(int rol, int neighrol, Role b) {
            this.neigbors[rol][neighrol] = b;

        }
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        HashMap<Integer,ArrayList<Integer>> hasRole = new HashMap<>();
        ArrayList<Role> roles = new ArrayList<>();
        int v = scan.nextInt();
        int s = scan.nextInt();
        int m = scan.nextInt();
        castGraph = new graph(v);
        scan.nextLine();
        for (int i = 0;i<v;i++){
            String[] line = scan.nextLine().trim().split(" ");
            int[] arr = new int[line.length-1];
            boolean hasdiv = false;
            for (int p = 1;p<line.length;p++){
                int num = Integer.parseInt(line[p]);
                arr[p-1] = num;
                if (num ==1 || num ==2) {
                    hasdiv = true;
                }
            }
            Role r = new Role(arr,i+1); 
            r.setcanBeDiva(hasdiv);
            r.setActor(0, false);
            roles.add(r);
            
        }
        
        
        
        
        
        for (int i = 0; i<s;i++) {
            String[] line = scan.nextLine().trim().split(" ");
            for (int j = 1;j< Integer.parseInt(line[0])+1;j++){
                for (int k = 1; k<Integer.parseInt(line[0])+1;k++) {
                    if (k!=j) {
                        castGraph.setNeighbors((Integer.parseInt(line[j])-1), Integer.parseInt(line[k])-1,roles.get(Integer.parseInt(line[k])-1));
                        
                    }
                }
            }
               
        }
        ArrayList<Role> sortedRoles = new ArrayList<>();
        for (Role r : roles){
            sortedRoles.add(r);
        }
        Collections.sort(sortedRoles, new Comparator<Role>(){
            public int compare(Role r1, Role r2) {
                if ((r1.candidates.length)-(r2.candidates.length)==0) {
                }
                return (r1.candidates.length)-(r2.candidates.length);
            }
        });
        

        
        scan.close();


        ArrayList<Integer> configs = new ArrayList<>();
        int countloops = 0;
        while (configs.size()<45 || countloops<1000) {
            boolean diva1stat = false;
            boolean diva2stat = false;
            Random rand = new Random();
            Role r1 = roles.get(rand.nextInt(roles.size()));
            Role r2 = roles.get(rand.nextInt(roles.size()));
            if (r1.rolenr!=r2.rolenr && r1.canBeDiva && r2.canBeDiva && castGraph.neigbors[r1.rolenr-1][r2.rolenr-1]==null && castGraph.neigbors[r2.rolenr-1][r1.rolenr-1]==null) {
                for (int c1 : r1.candidates) {
                    if (c1 == 1) {
                        diva1stat = true;
                        break;
                    }
                }
                for (int c2 : r2.candidates) {
                    if (c2 == 2) {
                        diva2stat = true;
                        break;
                    }
                }
                if (diva1stat && diva2stat) {
                    configs.add(r1.rolenr);
                    configs.add(r2.rolenr);
                }
            }
            countloops++;
        }
        for (Role r :roles) {
            if (r.actor == 0) {
                m++;
                r.setActor(m, false);
            }
        }
        int divoptc = 100000;
        int divoptr1 = 0;
        int divoptr2 = 0;
        for (int i = 0; i<configs.size();i=i+2) {
            Role r1 = roles.get(configs.get(i)-1);
            Role r2 = roles.get(configs.get(i+1)-1);
            int cans1 = r1.candidates.length;
            int cans2 = r2.candidates.length;
            if (cans1+cans2<divoptc){
                divoptc = cans1+cans2;
                divoptr1 = r1.rolenr;
                divoptr2 = r2.rolenr;

            }
            
        }
        roles.get(divoptr1-1).setActor(1, true);
        roles.get(divoptr1-1).setisSet();
        roles.get(divoptr2-1).setActor(2, true);
        roles.get(divoptr2-1).setisSet();
       
       
        for (Role r : sortedRoles) {  
            if (!r.getisSet()) {
                ArrayList<Integer> freq = new ArrayList<>();
                ArrayList<Integer> ind = new ArrayList<>();
                Role realr = roles.get(r.rolenr-1);
                for (int can : realr.candidates) {
                    if (allowed(roles, realr,v,can)) {
                        freq.add(localsearch(roles, realr, v, can));
                        ind.add(can);
                    }
                }
                if (freq.size()>0) {
                    int chosen = freq.indexOf(Collections.min(freq));
                    r.setActor(ind.get(chosen), false);
                    r.setisSet();
                } 
            }
        }
        
        
        for (Role r : roles){
            ArrayList<Integer> rollist = new ArrayList<>();
            if (!hasRole.containsKey(r.actor)) {
                rollist.add(r.rolenr);
                hasRole.put(r.actor,rollist);
            }
            else {
                rollist = hasRole.get(r.actor); 
                rollist.add(r.rolenr);
                hasRole.put(r.actor,rollist);
                }
            }
        System.out.println(hasRole.keySet().size());
        for (int actor : hasRole.keySet()) {
            int numRoles = hasRole.get(actor).size();
            String str = "";
            for (int Role : hasRole.get(actor)) {
                str += Role+" ";
            }
            System.out.println(actor+" "+numRoles+" "+str);   
        }
    }
    public static boolean allowed(ArrayList<Role> arr, Role r, int v, int c){
        if (c<3) {
            for (int i = 0; i<v;i++) {
                if (castGraph.neigbors[r.rolenr-1][i]!=null){
                   if (arr.get(i).getActor()<3) {
                        return false;
                   }
                }
                
            }
        }
        else {
            for (int i = 0; i<v;i++) {
            if (castGraph.neigbors[r.rolenr-1][i]!=null){
               if (arr.get(i).getActor()==c) {
                    return false;
               }
            }
            
        }
        }
        
        return true;
    }

    public static int localsearch(ArrayList<Role> arr, Role r, int v, int c) {
        int counter = 0;
        for (int i = 0; i<v;i++) {
            if (castGraph.neigbors[r.rolenr-1][i]!=null){
                for (int cNeighbour : arr.get(i).candidates){
                    if (cNeighbour==c) {
                        counter++;
                        break;
                    }
               }
            }
        }
        return counter;
    }
}

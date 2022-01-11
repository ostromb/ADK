import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class heuristic {
    public static ArrayList<role> roles;
    static class role {
        public boolean isDiva;
        public int actor;
        public boolean isSet;
        public int[] candidates;
        public int rolenr;
        public HashSet<Integer> nbrroles;
        public boolean canBeDiva;
        public role(int[] candidates,int rolenr) {
            this.candidates = candidates;
            this.isSet = false;
            this.rolenr = rolenr;
            this.nbrroles = new HashSet<Integer>();
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
        public void addNbr(int r) {
            this.nbrroles.add(r);
        }

    }
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        HashMap<Integer,ArrayList<Integer>> hasRole = new HashMap<>();
        roles = new ArrayList<>();
        int v = scan.nextInt();
        int s = scan.nextInt();
        int m = scan.nextInt();

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
            role r = new role(arr,i+1); 
            r.setcanBeDiva(hasdiv);
            r.setActor(0, false);
            roles.add(r);
            
        }
        for (int i = 0; i<s;i++) {
            String[] line = scan.nextLine().trim().split(" ");
            for (int j = 1;j< Integer.parseInt(line[0])+1;j++){
                for (int k = 1; k<Integer.parseInt(line[0])+1;k++) {
                    if (k!=j) {
                        roles.get((Integer.parseInt(line[j])-1)).addNbr(Integer.parseInt(line[k]));
                    }
                }
            }
               
        }
        scan.close();
        boolean notDivad = true;
        while (notDivad) {
            boolean diva1stat = false;
            boolean diva2stat = false;
            Random rand = new Random();
            role r1 = roles.get(rand.nextInt(roles.size()));
            role r2 = roles.get(rand.nextInt(roles.size()));
            if (r1.rolenr!=r2.rolenr && r1.canBeDiva && r2.canBeDiva && !r1.nbrroles.contains(r2.rolenr) ) {
                for (int c1 : r1.candidates) {
                    if (c1 == 1) {
                        diva1stat = true;
                        
                    }
                }
                for (int c2 : r2.candidates) {
                    if (c2 == 2) {
                        diva2stat = true;
                        
                    }
                }
                if (diva1stat && diva2stat) {
                    r1.setActor(1, true);
                    r1.setisSet();
                    r2.setActor(2, true);
                    r2.setisSet();
                    notDivad = false;
                }
            }
        }
      
        for (int i = 0; i<v;i++) {
            for (role r : roles) {  
                if (!r.getisSet()) {
                    Random rand = new Random();
                    try {
                    int can = r.candidates[(rand.nextInt(r.candidates.length)-i)];
                        if (can>2){
                            if (allowed(r,v,can)) {
                                if (!r.getisSet()) {
                                    r.setActor(can, false); 
                                }
                            }
                        }
                    }
                    catch (Exception e){
                    }
                   }
            }
        }
          
       
        for (role r :roles) {
            if (r.actor == 0) {
                m++;
                r.setActor(m, false);
                r.setisSet();
            }
        }
        for (role r : roles){
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
            for (int role : hasRole.get(actor)) {
                str += role+" ";
            }
            System.out.println(actor+" "+numRoles+" "+str);   
        }
    }
    public static boolean allowed(role r, int v, int c){
        for (int i = 0; i<v;i++) {
            if (r.nbrroles.contains(i+1)){
               if (roles.get(i).getActor()==c) {
                    return false;
               }
            }
            
        }
        return true;
    }
    public static boolean allsupers(role r, int v, int c){
        for (int i = 0; i<v;i++) {
            if (roles.get(i).getisSet()){
                    return false;
            }
            
        }
        return true;
    }
}

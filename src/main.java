import java.util.*;

public class main {
    static ArrayList<Partition> job;
    static HashMap<Integer,Integer> jobMap;
    static TreeMap<Integer,ArrayList<Partition>> empty;
    static ArrayList<Partition> emptyPart;
    static Scanner sc=new Scanner(System.in);
    static int totalFreeSpace;
    public static void main(String[] args) {
   	  job=new ArrayList<Partition>();
   	  jobMap=new HashMap<Integer,Integer>();
   	  empty=new TreeMap<Integer,ArrayList<Partition>>();
   	  emptyPart=new ArrayList<Partition>();
   	  job.add(new Partition(1,200,0));
   	  jobMap.put(0, 0);
   	  empty.put(800, new ArrayList<Partition>());
   	  empty.get(800).add(new Partition(201,1000));
   	  addEmptypart(new Partition(201,1000));
   	  totalFreeSpace=800;
   	  createJob();
   	  createJob();
   	  createJob();
   	  deleteJob();
   	  createJob();
   	 
    }
    public static void compaction() {
   	 //ArrayList<Partition> tempJob=new ArrayList<Partition>();
   	 int ind=1;
   	 for(int i=0;i<job.size();i++) {
   		 Partition p=job.get(i);
   		 int partitionSize=p.end-p.start+1;
   		 int shift=p.start-ind;
   		 p.start=ind;
   		 p.end=p.end-shift;
   		 ind+=partitionSize;
   		 System.out.println(job.get(i).start+" "+job.get(i).end);
   	 }
   	 emptyPart.clear();
   	 empty.clear();
   	 emptyPart.add(new Partition(ind,1000));
   	 empty.put(1000-ind+1, new ArrayList<Partition>());
   	 empty.get(1000-ind+1).add(new Partition(ind,1000));
   	 display();
    }
    public static void display() {
   	 System.out.println("EmptyPart ArrayList");
   	 for(int i=0;i<emptyPart.size();i++) {
   		 Partition p=emptyPart.get(i);
   		 System.out.println(p.start+" "+p.end);
   	 }
    }
    public static void addEmptypart(Partition p) {
   	 int i=0;
   	 for(;i<emptyPart.size();i++) {
   		 if(p.end<emptyPart.get(i).start) {
   			 break;
   		 }
   	 }
   	 emptyPart.add(i,p);
    }
    public static void createJob() {
   	 System.out.println("Id of new Process");
   	 int id=sc.nextInt();
   	 System.out.println("how much space is required for this JOB :");
   	 int temp=sc.nextInt();
   	 int space=Integer.MIN_VALUE;
   	 Iterator it=empty.entrySet().iterator();
   	 while(it.hasNext()) {
   		 Map.Entry p=(Map.Entry)it.next();
   		 int sp=(int)p.getKey();
   		 if(sp>temp) {
   			 System.out.println(sp+" "+space);
   			 space=sp;
   			 break;
   		 }
   	 }
   	 if(space==Integer.MIN_VALUE && totalFreeSpace>=temp) {
   		 System.out.println("Compaction Needed");
   		 compaction();
   		 return;
   	 }
   	 else if(totalFreeSpace<temp){
   		 System.out.println("There is no space to add this Job");
   	 }
   	 //System.out.println(space);
   	 Partition part=empty.get(space).get(0);
   	 empty.get(space).remove(0);
   	 if(empty.get(space).size()==0) {
   		 empty.remove(space);
   	 }
   	 //System.out.println(emptyPart.size());
   	 System.out.println(emptyPart.remove(part));
   	 //System.out.println(emptyPart.size());
   	 job.add(new Partition(part.start,part.start+temp-1,id));
   	 jobMap.put(id,job.size()-1);
   	 int remaining=part.end-part.start+1-temp;
   	 if(remaining!=0) {
   		 if(empty.containsKey(remaining)) {
   			 empty.get(remaining).add(new Partition(part.start+temp,part.end));
   		 }
   		 else {
   			 empty.put(remaining, new ArrayList<Partition>());
   			 empty.get(remaining).add(new Partition(part.start+temp,part.end));
   		 }
   		 addEmptypart(new Partition(part.start+temp,part.end));
   	 }
   	 totalFreeSpace-=temp;
   	 display();
    }
    public static void deleteJob() {
   	 System.out.println("Enter the id of Job which you want to delete");
   	 int id=sc.nextInt();
   	 int index=jobMap.get(id);
   	 Partition p=null;
   	 for(int i=0;i<job.size();i++) {
   		 if(job.get(i).jobid==id) {
   			 p=job.get(i);
   			 job.remove(i);
   			 break;
   		 }
   	 }
   	 if(p==null) {
   		 return;
   	 }
   	 
   	 //job.remove(index);
   	 int s=p.start;
   	 int e=p.end;
   	 totalFreeSpace+=(e-s+1);
   	 System.out.println(s+" "+e);
   	 for(int i=0;i<emptyPart.size();i++) {
   		 int st=emptyPart.get(i).start;
   		 int et=emptyPart.get(i).end;
   		 if(et==(s-1)) {
   			 s=st;
   			 empty.get(et-st+1).remove(emptyPart.get(i));
   			 if(empty.get(et-st+1).size()==0) {
   				 empty.remove(et-st+1);
   			 }
   			 emptyPart.remove(emptyPart.get(i));
   			 System.out.println(i);
   			 break;
   		 }
   		 else if(st>e) {
   			 break;
   		 }
   	 }
   	 for(int i=emptyPart.size()-1;i>=0;i--) {
   		 int st=emptyPart.get(i).start;
   		 int et=emptyPart.get(i).end;
   		 if(st==(e+1)) {
   			 e=et;
   			 empty.get(et-st+1).remove(emptyPart.get(i));
   			 if(empty.get(et-st+1).size()==0) {
   				 empty.remove(et-st+1);
   			 }
   			 emptyPart.remove(emptyPart.get(i));
   			 System.out.println(i+" hiiiii");
   			 break;
   		 }
   		 else if(s>et) {
   			 break;
   		 }
   	 }
   	 Partition temp=new Partition(s,e);
   	 addEmptypart(temp);
   	 int remaining=e-s+1;
   	 if(empty.containsKey(remaining)) {
   		 empty.get(remaining).add(temp);
   	 }
   	 else {
   		 empty.put(remaining, new ArrayList<Partition>());
   		 empty.get(remaining).add(temp);
   	 }
   	 //addEmptypart(temp);
   	 
   	 display();
   	 
    }
    

}

class Partition implements Comparable<Partition>{
    int start;
    int end;
    int jobid;
    Partition(int s,int e,int id){
   	 this.start=s;
   	 this.end=e;
   	 this.jobid=id;
    }
    Partition(int s,int e){
   	 this.start=s;
   	 this.end=e;
   	 this.jobid=-1;
    }
    @Override
    public int compareTo(Partition o) {
   	 int s=o.start;
   	 int e=o.end;
   	 if(this.start==s && this.end==e) {
   		 return 0;
   	 }
   	 else if(this.end<s ) {
   		 return -1;
   	 }
   	 else if(this.start>e ) {
   		 return 1;
   	 }
   	 else
   	 {
   		 System.out.println("This partitions are not comparable");
   		 return 0;
   	 }
   	 
    }
    @Override
	public boolean equals(Object o) {
   	 Partition c = (Partition) o;
    	if(this.start==c.start && this.end==c.end) {
   		 return true;
   	 }
    	else return false;
   	 
	}
}


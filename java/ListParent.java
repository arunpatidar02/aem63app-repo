import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListParent {

	public static void main(String[] args) {
		
		List<String> list = new ArrayList<String>();
		list.add("/content/site/en/page1");
		list.add("/content/site/en/page1/page2/page3");
		list.add("/content/site/en/page1/page2/page3/page4/page5");
		list.add("/content/site/en2/page1/page2");
		list.add("/content/site/en/page1/page2/page3/page4");
		list.add("/content/site/en");
		
		Collections.sort(list);
		Collections.reverse(list);
	
		List<String> list3 = new ArrayList<String>();
		
		Iterator<String> it = list.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		
		
		for (int i = 0; i < list.size(); i++) {
			boolean found=false;
			  for (int j = i+1; j < list.size(); j++) {
				  if(list.get(i).indexOf(list.get(j).concat("/"))==0) {
					  found = true;
					  break;
				  }
			  }
			  if(!found) {
				  list3.add(list.get(i));
			  }
			}
		
		Iterator<String> it3 = list3.iterator();
		System.out.println("-----------");
		while(it3.hasNext()) {
			System.out.println(it3.next());
		}
		

	}

}


/* Output
-----------
/content/site/en2/page1/page2
/content/site/en/page1/page2/page3/page4/page5
/content/site/en/page1/page2/page3/page4
/content/site/en/page1/page2/page3
/content/site/en/page1
/content/site/en
-----------
/content/site/en2/page1/page2
/content/site/en
*/

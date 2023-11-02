package anudip.MobilePurchaseSystem.dao;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import anudip.MobilePurchaseSystem.entity.Mobiles;
import anudip.MobilePurchaseSystem.entity.Purchasedetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class Daoimpl 
{
	
	Purchasedetails pd=new Purchasedetails();
	Scanner sc=new Scanner(System.in);
	
	//method to insert data in Mobiles table
	public void insertmobile()
	{
		//create an EntityManagerFactory
		EntityManagerFactory ef=Persistence.createEntityManagerFactory("pu");
		
		//create an EntityManager
		EntityManager em=ef.createEntityManager();
		
		//obj of Mobileclass
		Mobiles mob=new Mobiles();
		
		//begin a new transaction
		em.getTransaction().begin();
		
		int mobileid;
		String name;
		int price;
		int quantity;
		sc.nextLine();
		
		System.out.println("Enter name of mobile:");
		name=sc.nextLine();
		
		System.out.println("Enter quantity:");
		quantity=sc.nextInt();
		
		System.out.println("Enter mobileid:");
		mobileid=sc.nextInt();
		
		System.out.println("Enter price:");
		price=sc.nextInt();
		
		//setting data into table
		mob.setMobileid(mobileid);
		mob.setName(name);
		mob.setPrice(price);
		mob.setQuantity(quantity);
		
		em.persist(mob);
		
		//commit the transaction
		em.getTransaction().commit();
		System.out.println("record inserted successfully "+mob);
		
		//close entitymanager and entitymanagerfactory
		ef.close();
		em.close();
		
	}
	
	//method to insert data in purchasedetails table
	public void insertpurchasedetails()
	{
		//create an EntityManagerFactory
		EntityManagerFactory ef=Persistence.createEntityManagerFactory("pu");
		
		//create an EntityManager
		EntityManager em=ef.createEntityManager();
		//begin a new transaction
		em.getTransaction().begin();
		
		
		String cname;
		String mailId;
		String phoneno;
		int mobileid;
		
		System.out.println("Enter name of cname:");
		cname=sc.nextLine();
		
		//validating name
		Pattern pattern=Pattern.compile("^[A-Z][a-z]{0,19}$");
		Matcher matchern=pattern.matcher(cname);
		//if name is correct the take email else return
		if(matchern.matches())
		{
			System.out.println("Enter mailId:");
			mailId=sc.nextLine();
			
			//validating mail
			Pattern patternm=Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
			Matcher matcherm=patternm.matcher(mailId);
			//if mail is correct the take email else return
			if(matcherm.matches())
			{
				System.out.println("Enter phoneno:");
				phoneno=sc.nextLine();
				
				//validating mail
				Pattern patternp=Pattern.compile("^[0-9]{10}$");
				Matcher matcherp=patternp.matcher(phoneno);
				//if phoneno is correct the take email else return
				if(matcherp.matches())
				{
					System.out.println("Enter mobileid:");
					mobileid=sc.nextInt();
					
					//obj of mobile class
					Mobiles mobile=em.find(Mobiles.class, mobileid);
					
					//if quantity>0 then allow purchasing
					if(mobile!=null&&mobile.getQuantity()>0)
					{
						pd.setCname(cname);
						pd.setMailId(mailId);
						pd.setPhoneno(phoneno);
						pd.setMobiles(mobile);
						
						em.persist(pd);
					}
					else
					{
						System.out.println("mobile quantity is not sufficent");
					}
				}
				else
				{
					System.out.println("details are invalid");
					return;
				}
			}
			else
			{
				System.out.println("details are invalid");
				return;
			}	
		}
		else
		{
			System.out.println("details are invalid");
			return;
		}
		//commit the transaction
		em.getTransaction().commit();
		System.out.println("record inserted successfully "+pd);
		//close entitymanager and entitymanagerfactory
		ef.close();
		em.close();
		
		
	}
	public void delete(int id)
	{
		//create an EntityManagerFactory
		 EntityManagerFactory ef=Persistence.createEntityManagerFactory("pu");
		 
		//create an EntityManager
		 EntityManager em=ef.createEntityManager();
		 Mobiles mobdel=new Mobiles();
		 
		//begin a new transaction
		 em.getTransaction().begin(); 
		 //finding record based on id
		 mobdel=em.find(Mobiles.class,id);
		 
		 if(mobdel!=null)
		 {
			 //removing record based on id if not null
			 em.remove(mobdel);
			 System.out.println("record deleted succesfully");
		 }
		 else
		 {
			 System.out.println("mobile of this id are not available");
		 }
		//commit the transaction
		 em.getTransaction().commit();
		 
		//close entitymanager and entitymanagerfactory
		 em.close();
		 ef.close();
	}
	
	//method to update data in mobiles table
	public void update(int id)
	{
		//create an EntityManagerFactory
		 EntityManagerFactory ef=Persistence.createEntityManagerFactory("pu");
		 
		//create an EntityManager
		 EntityManager em=ef.createEntityManager();
		 
		//begin a new transaction
		 em.getTransaction().begin(); 
		//finding record based on id
		 Mobiles mob1=em.find(Mobiles.class,id);
		//updating record based on id if not null and quqntity>0
		 if(mob1!=null&&mob1.getQuantity()>0)
		 {
		 System.out.println("enter quantity of mobile to update the record:"); 
		 int z=mob1.getQuantity()-1;
		 mob1.setQuantity(z);
		 }
		 else
		 {
			 System.out.println("mobile of this id are not available");
		 }
		//commit the transaction
		 em.getTransaction().commit();
		//close entitymanager and entitymanagerfactory
		 em.close();
		 ef.close();
	}
	
	//method to search mobile details using price range
	public void search()
	{
		//create an EntityManagerFactory
		EntityManagerFactory ef=Persistence.createEntityManagerFactory("pu");
		
		//create an EntityManager
		EntityManager em=ef.createEntityManager();
		
		//begin a new transaction
		em.getTransaction().begin();
		System.out.println("enter min price:");
		int min_price=sc.nextInt();
		System.out.println("enter max price:");
		int max_price=sc.nextInt();
		//query for rangeofprice
		TypedQuery<Mobiles> query=em.createQuery("select mob from Mobiles mob where mob.price between :min_price and :max_price",Mobiles.class);
		query.setParameter("max_price",max_price );
		query.setParameter("min_price",min_price );
		//list storing all mobiles in given range
		List<Mobiles> mobileinprice=query.getResultList();
		if(mobileinprice.isEmpty())
		{
			 System.out.println("mobile of this price range are not available");
		}
		else
		{
			System.out.println("mobile of this price range are:");
			//printing list
			for(Mobiles mobile:mobileinprice)
			{
				System.out.println("mobileId: "+mobile.getMobileid()+" mobilename: "+mobile.getName()+" price: "+mobile.getPrice());
			}
		}
		//commit the transaction
		em.getTransaction().commit();
		//close entitymanager and entitymanagerfactory
		ef.close();
		em.close();
	}
	
	//method to view mobile data
	public List<Mobiles> display()
	{
		//create an EntityManagerFactory
		EntityManagerFactory ef=Persistence.createEntityManagerFactory("pu");
		
		//create an EntityManager
		EntityManager em=ef.createEntityManager();
		
		//begin a new transaction
		em.getTransaction().begin();
		Query query=em.createNamedQuery("view");
		@SuppressWarnings("unchecked")
		List<Mobiles> list=query.getResultList();
		//close entitymanager and entitymanagerfactory
		ef.close();
		em.close();
		return list;
	}
}

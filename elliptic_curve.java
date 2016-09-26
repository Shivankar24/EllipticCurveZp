import java.io.*;
import java.math.BigInteger;
import java.util.*;
public class elliptic_curve {
	
	/** 
	 * This class is used to create an elliptic curve object, of the type 
	 * y^2=x*x*x+A*x+B mod p, where p is a prime, and then generate all the points on it and store them in the arraylist
	 * list_of_points. 
	 * It also finds the point on the Elliptic curve with the highest order. 
	 * 
	 * @author      Shivankar Ojha
	 * 
	 
	 */

	public static int p;
	public static int A;
	public static int B;
	//Array list to store all points found on Elliptic curve
	static ArrayList<ArrayList<Integer>> list_of_points;
	public elliptic_curve(int p2, int a2, int b2) {
		// TODO Auto-generated constructor stub
		p=p2;
		A=a2;
		B=b2;
		list_of_points=new ArrayList<ArrayList<Integer>>();
	}

	public static BigInteger mod_inverse(BigInteger a, BigInteger n)
	{
		
		BigInteger t=BigInteger.ZERO, newt=BigInteger.ONE,r=n,newr=a;BigInteger quotient=BigInteger.ZERO;
		t=a.modInverse(n);
		return t;
	}

	public static int equation_value(int x)
	{
		
		//Equation of the elliptic curve modulo prime p
		
		int answer=x*x*x+A*x+B;
		answer=answer%p;
		return answer;
	}
	public static boolean quad_residue(int x)
	{
		//Function which checks whether an input 
		//x given to it is a quadratic residue or not. 
		
		int num=(p-1)/2,answer=1;
		for (int i=1;i<=num;i++)
		{
			answer=answer*x;
			answer=answer%p;
		}
		
		if (answer==1)
			return true;
		else
			return false;
		
	}
	public static int SquareAndMult(int x,int i,int n)
	{
		//function implementing the square and multiply technique
		
		int answer=1;
		for (int j=1;j<=i;j++)
		{
		answer=answer*x;
		answer=answer%n;
		}
		return answer;
	}
	public static ArrayList<Integer> point_addition(ArrayList<Integer> point1,ArrayList<Integer> point2)
	{
		//This function returns the point which is 
		//the result of the addition of two points. 
		
		//Array List to store the coordinates of the new point. 
		ArrayList<Integer> new_point=new ArrayList<Integer>();
		
		//Lambda is basically the slope of the line
		//passing through the two points. 
		int lambda;
		
		//x1,x2,y1,y2 store the respective x and y
		//values of the two points to be added. 
		
		int x1=point1.get(0),y1=point1.get(1);
		int x2=point2.get(0),y2=point2.get(1);
		
		BigInteger bi=new BigInteger(""+0);
		//if the points are inverse of each other 
		//then I have used Null to denote O
			
		if (x1==x2 && y2==p-y1)
			return null;
		
		//if the points are same, we calculate the 
		//slope of the line accordingly. 
		
		if (x1==x2 && y2==y1)
		{
			bi=new BigInteger(""+(2*y1));
			bi=mod_inverse(bi,new BigInteger(""+p));
			lambda=(3*x1*x1+1)*bi.intValue();
			lambda=lambda%p;
		}		
		else 
		{
			bi=new BigInteger(""+(x2-x1));
			bi=mod_inverse(bi,new BigInteger(""+p));
			lambda=(y2-y1)*bi.intValue();
			lambda=lambda%p;
		}
		
		//calculation of the coordinates of the new point obtained 
		//after addition is done as follows: 
	
		int x3=lambda*lambda-x1-x2;
		x3=x3%p;
		
		if (x3<0)
			x3=x3+p;
		int y3=lambda*(x1-x3)-y1;
		y3=y3%p;

		if (y3<0)
			y3=y3+p;
		new_point.add(x3);
		new_point.add(y3);
		
		
		return new_point;
	}
	public static void main(String[] args)throws IOException {
		// TODO Auto-generated method stub

		BufferedReader inp=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the value of prime p:");
		int p=Integer.parseInt(inp.readLine());
		System.out.println("Enter the value of A for Elliptic curve type: y^2=x*x*x+A*x+B");
		int A=Integer.parseInt(inp.readLine());
		System.out.println("Enter the value of B for Elliptic curve type: y^2=x*x*x+A*x+B");
		int B=Integer.parseInt(inp.readLine());
		elliptic_curve ec=new elliptic_curve(p,A,B);
		
		ArrayList<Integer> point1=new ArrayList<Integer>();
		ArrayList<Integer> point2=new ArrayList<Integer>();
		int no_of_points=0;
		for (int x=0;x<p;x++)
		{
			//This computes the value of the EC equation
			//modulo p
			int answer=equation_value(x);
			int z1=0,z2=0;
			
			//Quadratic residue check
			if (quad_residue(answer))
			{
				//If it is a quadratic residue, 
				//we find the roots and two points 
				//as follows: 
				point1=new ArrayList<Integer>();
				point2=new ArrayList<Integer>();
				
				z1=SquareAndMult(answer, (p+1)/4, p);
				z2=p-1*z1;
				point1.add(x);point1.add(z1);
				point2.add(x);point2.add(z2);
				list_of_points.add(point1);
				list_of_points.add(point2);
				//Incrementing the number of points by 2. 
				no_of_points=no_of_points+2;
			}
			
		}
		System.out.println("The points on the elliptic curve: ");
		for (ArrayList i: list_of_points)
			System.out.println(i);
		System.out.println("Total number of points: "+ list_of_points.size());
		
		//variables which are used for finding the
		//largest order element.
		int largest_order=0;
		ArrayList<ArrayList<Integer>> largest_order_element=new ArrayList<ArrayList<Integer>>();
		
		
		for (int i=0;i<list_of_points.size();i++)
		{
			point1=new ArrayList<Integer>();
			point2=new ArrayList<Integer>();
			point1.addAll(list_of_points.get(i));
			point2.addAll(point1);
			int order=1;
			int flag=0;
			do
			{
				//we keep on adding the point with itself
				//till we get the unit element 
				//which in this case i have taken as null. 
				//the moment that happens, we have found the 
				//order of the element in consideration. 
				order++;
				point2=point_addition(point1, point2);
				if (order>p)
				{
					flag=1;
					break;
				}
			}while(point2!=null);
			if (flag==1)
				continue;
			else
				System.out.println("Order for point "+list_of_points.get(i)+": "+order);
			
			//check for the largest order 
			//store the equivalent point on EC. 
			if (order>largest_order)
			{
				largest_order_element=new ArrayList<ArrayList<Integer>>();
				largest_order=order;
				largest_order_element.add(point1);
			}
			if (order==largest_order)
			{
				largest_order_element.add(point1);
			}

		}
		
			System.out.println("Largest order: "+largest_order);
			System.out.println("Largest order element: "+largest_order_element);
			
			
		
	}

}

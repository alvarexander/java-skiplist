// Alexander Alvarez: AL304208
// COP3503, Spring 2017, University of Central Florida
// SkipList - Assignment 4


import java.util.*;



class Node <AnyType>
{
  private AnyType data;
  private int height;
  public List<Node<AnyType>> next_nodes = new ArrayList<Node <AnyType>>();


  // Constructor useful for the creation of a head node, that holds no data.
  Node (int height)
  {

    this.height = height;

    // Initially all next pointers must be null
    for (int i = 0; i <= height; i++)
    {
    	this.next_nodes.add(null);
    }


  }
// Constructor for the creation of nodes with a specified height and data
  Node(AnyType data, int height)
  {
	  this.data = data;
	  this.height = height;
	// Initially all next pointers must be null
	  for (int i = 0; i <= height; i++)
	  {
	      this.next_nodes.add(null);
	  }
  }

  public AnyType value()
  {
	  return this.data;
  }

  public int height()
  {
	  return this.height;
  }

  public Node<AnyType> next(int level)
  {
	// If it's out of bounds, return null
	 if ( level < 0 || level >  this.height() - 1)
		 return null;

	 return this.next_nodes.get(level);


  }

  public void setNext(int level, Node<AnyType> node)
  {
	  this.next_nodes.set(level, node);

  }


  public void grow()
  {
	  this.height = this.height + 1;
	  this.next_nodes.add(height - 1, null);
  }

  public void maybeGrow()
  {
	  if(Math.random () < 0.5) // Probability of 50%
	  {
		 this.height = this.height + 1;
		 this.next_nodes.add(this.height -1, null); // Add null reference
	  }
  }

 // Trim the a node's height to specified paramater
  public void trim(int height)
  {
	  int i;
	  // By starting at height and ending at the local paramater 'height' -
	  // you reduce the level to the parameter by setting those to null references
	  for (i = height ; i < this.height; i++)
	  {
		  	this.next_nodes.set(i, null);
	  }

	  this.height = height;
  }
}
// Skiplist class that is generic and hold nodes with any type of data
public class SkipList <AnyType extends Comparable<AnyType>>
{

	private int height; // At most ceiling of log base(2) of n
	private int size;
	private Node<AnyType> head;

  // Constructor for the creation of a SkipList with default height (1)
  SkipList()
  {
	 this.height = 1; // default height of 1
	 this.head = new Node<AnyType>(height);

  }
  // Constructor for the creation of a SkipList with specified height
  SkipList(int height)
  {
	  this.height = height;
	  this.head = new Node <AnyType> (height);
  }

  public int size()
  {
	  return this.size;
  }

  public int height()
  {
	  return this.height;
  }

 public Node<AnyType> head()
 {
	 return this.head;
 }


 public void insert(AnyType data)
 {
	 int i, new_height, node_height, level, new_level;
	 // Keep a "map" of nodes for re-connecting them later
	 HashMap <Integer,Node<AnyType>> hold_nodes = new HashMap <Integer, Node<AnyType>> ();
	 Node<AnyType> node =  this.head;


	 this.size = this.size + 1;  // A newly added node increases the size by 1


	 // The new height is limited by the ceiling of the log base 2 of n
	 new_height = (int)Math.ceil((Math.log(this.size) / Math.log(2)));

	 // If the newly created height is greater than the original, grow your SkipList
	 if (new_height > this.height)
	 {
			this.height = new_height;
			this.head.grow(); // Grow head when the maximum height has been increased


			Node<AnyType> holder = this.head;
			Node<AnyType> buffer = this.head.next(this.height - 2);


			while(buffer != null)
			{
				buffer.maybeGrow();// Provides 50% chance of growth by +1

				if(buffer.height() == (this.height - 1) + 1)
				{
					//Connect the buffer if it grew
					holder.setNext((this.height - 1), buffer);

					holder = holder.next((this.height - 1));
				}
				// Continue iterations
				buffer = buffer.next((this.height - 1));
			}
	 }

	 level = this.height - 1;


	 // Add the node to the SkipList
	 while (node != null && level >= 0 )
	 {
		 if(node.next(level) == null)
		 {
			 hold_nodes.put(level,node); // Map the path
			 level = level - 1; // Go down one level

		 }

		 else  if (node.next(level).value().compareTo(data) < 0)
		 {
			 node = node.next(level);
		 }


		 else if(node.next(level).value().compareTo(data) >= 0)
		 {
			 hold_nodes.put(level,node); // Map the path
			 level = level - 1; // Go down one level

		 }
	 }

	 node_height = generateRandomHeight(this.height);

	 Node<AnyType> new_node = new Node<AnyType> (data,node_height);

	 // Use HashMap to reconnect references
	 for ( i = node_height - 1; i >= 0; i--)
	 {
		 new_node.setNext(i, hold_nodes.get(i).next(i));
		 hold_nodes.get(i).setNext(i, new_node);
	 }






 }

 // Same as above but with specified height, not randomly generated
 public void insert(AnyType data, int height)
 {
  int i, new_height, node_height, level, new_level;
	 // Keep a "map" of nodes for re-connecting them later
	 HashMap <Integer,Node<AnyType>> hold_nodes = new HashMap <Integer, Node<AnyType>> ();
	 Node<AnyType> node =  this.head;


	 this.size = this.size + 1;  // A newly added node increases the size by 1


	 // The new height is limited by the ceiling of the log base 2 of n
	 new_height = (int)Math.ceil((Math.log(this.size) / Math.log(2)));

	 // If the newly created height is greater than the original, grow your SkipList
	 if (new_height > this.height)
	 {
			this.height = new_height;
			this.head.grow(); // Grow head when the maximum height has been increased


			Node<AnyType> holder = this.head;
			Node<AnyType> buffer = this.head.next(this.height - 2);


			while(buffer != null)
			{
				buffer.maybeGrow();// Provides 50% chance of growth by +1

				if(buffer.height() == (this.height - 1) + 1)
				{
					//Connect the buffer if it grew
					holder.setNext((this.height - 1), buffer);

					holder = holder.next((this.height - 1));
				}
				// Continue iterations
				buffer = buffer.next((this.height - 1));
			}
	 }


	 level = this.height - 1;


	 // Add the node to the SkipList
	 while (node != null && level >= 0 )
	 {
		 if(node.next(level) == null)
		 {
			 hold_nodes.put(level,node); // Map the path
			 level = level - 1; // Go down one level
		 }

		 else  if (node.next(level).value().compareTo(data) < 0)
		 {
			 node = node.next(level);
		 }

		 else if(node.next(level).value().compareTo(data) >= 0)
		 {
			 hold_nodes.put(level,node); // Map the path
			 level = level - 1; // Go down one level

		 }
	 }



	 Node<AnyType> new_node = new Node<AnyType> (data,height);

	 // Use HashMap to reconnect references
	 for ( i = height - 1; i >= 0; i--)
	 {
		 new_node.setNext(i, hold_nodes.get(i).next(i));
		 hold_nodes.get(i).setNext(i, new_node);
	 }




 }

  public int generateRandomHeight(int height)
 {

       int i, random_height;

       for (i = 0; i < height; i++ )
       {
    	   if(Math.random() < 0.5)
    		   break;

       }

       random_height = i;

       return random_height;
}

// Recurse backwards to trim the list
 private void trimSkipList(Node<AnyType> node)
 {
	 if (node == null)
		 return;

	 int level = this.height - 1;

	 trimSkipList(node.next(level));

	 node.trim(this.height);
 }


 public void delete(AnyType data)
 {
	 // Keep a "map" of nodes for re-connecting them later
	HashMap <Integer, Node<AnyType>> node_map = new HashMap <Integer,Node<AnyType>> ();

	 int level, new_level, new_height,i;
	 Node <AnyType> node = this.head;
	 level = this.height - 1;

	 while (level >= 0 && node != null)
	 {
		 if(node.next(level) == null)
		 {
			  level = level - 1;

		 }

		 else if(node.next(level).value().compareTo(data) > 0)
		 {
			 level = level - 1;
		 }

		 else if (node.next(level).value().compareTo(data) < 0)
		 {
			 level = level - 1;
		 }

		 else
		 {
			 node_map.put(level,node);
			 level = level - 1;
		 }


	 }

	 // Move to the node we are deleting
	 node = node.next(level+1);

	 if(node != null && node.value().compareTo(data) == 0)
	 {
		 new_level = node.height();
		 for (i = new_level - 1; i >= 0; i--)
		 {
			 node_map.get(i).setNext(i,node.next(i));

		 }

		 this.size = this.size - 1; // A deleted element decreases size by 1

		 if(this.size <= 1)
			 new_height = 1;

		 else
			 new_height = (int)Math.ceil((Math.log(this.size) / Math.log(2)));

		 if(new_height < this.height)
		 {
			 this.height = new_height;

			 this.trimSkipList(this.head);
		 }

	 }



 }


 public boolean contains(AnyType data)
 {

	 	int level = this.height-1;

		Node<AnyType> node = this.head;

		while(level >= 0 && node != null)
		{

			if(node.next(level) == null)
			{
				level = level -1;
			}


			else if(node.next(level).value().compareTo(data) < 0)
			{
				node = node.next(level);
			}


			else if(node.next(level).value().compareTo(data) > 0)
			{
				level = level -1;
			}

			else
			{
				return true;
			}
		}
		return false; // Not found, return false
	}



 public Node<AnyType> get(AnyType data)
 {
	 int level = this.height - 1;

	 Node <AnyType> node = this.head;

	 // If the data is not contained in the SkipList, simply return false
	 if(contains(data) == false)
		 return null;
	 else

			while(level >= 0 && node != null)
			{

				if(node.next(level) == null)
				{
					level = level -1;
				}

				else if(node.next(level).value().compareTo(data) < 0)
				{
					node = node.next(level);
				}

				else if(node.next(level).value().compareTo(data) > 0)
				{
					level = level -1;
				}

				else
				{
					break;
				}
			}
		 return node;


 }

 public static double difficultyRating()
 {
   return 5.0;
 }

 public static double hoursSpent()
 {
    return 25;
 }

}

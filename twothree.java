import java.io.*;
import java.util.ArrayList; 

class Node {
   String guide;
   // guide points to max key in subtree rooted at node
}

class InternalNode extends Node {
   Node child0, child1, child2;
   // child0 and child1 are always non-null
   // child2 is null iff node has only 2 children
}

class LeafNode extends Node {
   // guide points to the key

   int value;
}

class TwoThreeTree {
   Node root;
   int height;

   TwoThreeTree() {
      root = null;
      height = -1;
   }
}

class WorkSpace {
// this class is used to hold return values for the recursive doInsert
// routine (see below)

   Node newNode;
   int offset;
   boolean guideChanged;
   Node[] scratch;
}

public class twothree {

   public static void main(String[] args) throws IOException {
//       First create a new tree to store all the data
       TwoThreeTree tree = new TwoThreeTree();
//       Reading from a file
//       File input = new File("/Users/Sakib/Desktop/2-3-tree-range-query-sample-io/test2.in");
//       BufferedReader reader = new BufferedReader(new FileReader(input));
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       String line = reader.readLine();
//       The first line is the database size 
       int databaseSize = Integer.parseInt(line);
//       Iterate through the file and add every line to the tree
       for (int i = 0; i < databaseSize; i++) {
           line = reader.readLine();
           String[] array = line.split(" ");
           insert(array[0], Integer.parseInt(array[1]), tree);
       }
//       Testing the addAll method 
//       ArrayList<LeafNode> output = addAll(tree.root, tree.height, new ArrayList<LeafNode>());
//       System.out.println(output.size());
       
//       Testing the printLe method 
//       ArrayList<LeafNode> less = printLe(tree.root, "z", tree.height, new ArrayList<LeafNode>());
//       System.out.println(less.size());
       
//       Testing printGe method 
//       ArrayList<LeafNode> great = printGe(tree.root, "g", tree.height, new ArrayList<LeafNode>());
//       System.out.println(great.size());
       
//       Testing printRange method
//       ArrayList<LeafNode> range = printRange(tree.root, "g", "k", tree.height, new ArrayList<LeafNode>());
//       System.out.println(range.size());
       
//       System.out.println("Root: " + tree.root.guide);
//       System.out.println("Child0: " + iNode.child0.guide);
//       System.out.println("Child1: " + iNode.child1.guide);
//       System.out.println("Child3: " + iNode.child2.guide);
       
       
       int querySize = Integer.parseInt(reader.readLine());
//       String[][] queries = new String[querySize][2];
       
//       The output that will be flushed
       BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);
//       Replace the i with querysize once done testing
       for (int i = 0; i < querySize; i++) {
           line = reader.readLine();
           String[] array = line.split(" ");
           String first;
           String second;
           if (array[0].compareTo(array[1]) < 0) {
               first = array[0];
               second = array[1];
           }
           else {
               first = array[1];
               second = array[0];
           }
           printRange(tree.root, first, second, tree.height, output);
       }
       output.flush();
       reader.close();
   }
   
//   Remove print statements from this 
   static void printRange(Node node, String x, String y, int height, BufferedWriter out) throws IOException {
       if (height == 0) {
           LeafNode LNode = (LeafNode) node;
           if (LNode.guide.compareTo(x) >= 0 && LNode.guide.compareTo(y) <= 0) {
               out.write(LNode.guide + " " + LNode.value + "\n");
           }
       }
       else {
           InternalNode iNode = (InternalNode) node;
           if (iNode.child0.guide.compareTo(y) >= 0) {
               printRange(iNode.child0, x, y, height - 1, out);
           }
           else if (iNode.child2 == null || iNode.child1.guide.compareTo(y) >= 0) {
               if (iNode.child0.guide.compareTo(x) >= 0) {
                   printGe(iNode.child0, x, height - 1, out);
                   printLe(iNode.child1, y, height - 1, out);
               }
               else {
                   printRange(iNode.child1, x, y, height - 1, out);
               }
           }
           else {
               if (iNode.child0.guide.compareTo(x) >= 0) {
                   printGe(iNode.child0, x, height - 1 , out);
                   addAll(iNode.child1, height - 1, out);
                   printLe(iNode.child2, y, height-1, out);
               }
               else if (iNode.child1.guide.compareTo(x) >= 0) {
                   printGe(iNode.child1, x, height - 1, out);
                   printLe(iNode.child2, y, height - 1, out);
               }
               else {
                   printRange(iNode.child2, x, y, height - 1, out);
               }
           }
       }
   }
   
   static void printLe(Node node, String value, int height, BufferedWriter out1) throws IOException {
       if (height == 0) {
           if (node.guide.compareTo(value) <= 0) {
               LeafNode Lnode = (LeafNode) node;
               out1.write(Lnode.guide + " " + Lnode.value + "\n");
           }
       }
       else {
           InternalNode iNode = (InternalNode) node;
           if (iNode.child0.guide.compareTo(value) >= 0) {
               printLe(iNode.child0, value, height - 1, out1);
           }
           else if (iNode.child2 == null || iNode.child1.guide.compareTo(value) >= 0) {
               addAll(iNode.child0, height-1, out1); 
               printLe(iNode.child1, value, height-1, out1);
           }
           else {
               addAll(iNode.child0, height-1, out1); 
               addAll(iNode.child1, height-1, out1); 
               printLe(iNode.child2, value, height-1, out1);
           }
       }
   }
   
   static void printGe(Node node, String value, int height, BufferedWriter out2) throws IOException {
       if (height == 0) {
           if (node.guide.compareTo(value) >= 0) {
               LeafNode Lnode = (LeafNode) node;
               out2.write(Lnode.guide + " " + Lnode.value + "\n");
           }
       }
       else {
           InternalNode iNode = (InternalNode) node;
           if (iNode.child0.guide.compareTo(value) >= 0) {
//               Handle the case where some of the values in the subtree
//               will be legit
               printGe(iNode.child0, value, height - 1, out2);
               addAll(iNode.child1, height - 1, out2);
               if (iNode.child2 != null) {
                   addAll(iNode.child2, height-1, out2);
               }
           }
//           Else the first child can be ignored 
//           Check the second child
           else if (iNode.child2 == null || iNode.child1.guide.compareTo(value) >= 0) {
//               Handle the case where some of the values in the subtree
//               will be legit
               printGe(iNode.child1, value, height - 1, out2);
               if (iNode.child2 != null) {
                   addAll(iNode.child2, height-1, out2);
               }
           }
//           The first two children can be ignored
           else {
               if (iNode.child2 == null || iNode.child2.guide.compareTo(value) < 0) {
                   return;
               }
               else {
//                   Handle case where some vaues in the third subtree are legit
                   printGe(iNode.child2, value, height - 1, out2);
               }
               
           }
       }
       

   }
   
   static void addAll (Node node, int height, BufferedWriter out3) throws IOException {
       if (height == 0) {
           LeafNode LNode = (LeafNode)node;
           out3.write(LNode.guide + " " + LNode.value + "\n");
       }
       else {
           InternalNode iNode = (InternalNode) node;
           addAll(iNode.child0, height - 1, out3);
           addAll(iNode.child1, height - 1, out3);
           if (iNode.child2 != null) {
               addAll(iNode.child2, height - 1, out3);
           }
       }
   }

   static void insert(String key, int value, TwoThreeTree tree) {
   // insert a key value pair into tree (overwrite existsing value
   // if key is already present)

      int h = tree.height;

      if (h == -1) {
          LeafNode newLeaf = new LeafNode();
          newLeaf.guide = key;
          newLeaf.value = value;
          tree.root = newLeaf; 
          tree.height = 0;
      }
      else {
         WorkSpace ws = doInsert(key, value, tree.root, h);

         if (ws != null && ws.newNode != null) {
         // create a new root

            InternalNode newRoot = new InternalNode();
            if (ws.offset == 0) {
               newRoot.child0 = ws.newNode; 
               newRoot.child1 = tree.root;
            }
            else {
               newRoot.child0 = tree.root; 
               newRoot.child1 = ws.newNode;
            }
            resetGuide(newRoot);
            tree.root = newRoot;
            tree.height = h+1;
         }
      }
   }

   static WorkSpace doInsert(String key, int value, Node p, int h) {
   // auxiliary recursive routine for insert

      if (h == 0) {
         // we're at the leaf level, so compare and 
         // either update value or insert new leaf

         LeafNode leaf = (LeafNode) p; //downcast
         int cmp = key.compareTo(leaf.guide);

         if (cmp == 0) {
            leaf.value = value; 
            return null;
         }

         // create new leaf node and insert into tree
         LeafNode newLeaf = new LeafNode();
         newLeaf.guide = key; 
         newLeaf.value = value;

         int offset = (cmp < 0) ? 0 : 1;
         // offset == 0 => newLeaf inserted as left sibling
         // offset == 1 => newLeaf inserted as right sibling

         WorkSpace ws = new WorkSpace();
         ws.newNode = newLeaf;
         ws.offset = offset;
         ws.scratch = new Node[4];

         return ws;
      }
      else {
         InternalNode q = (InternalNode) p; // downcast
         int pos;
         WorkSpace ws;

         if (key.compareTo(q.child0.guide) <= 0) {
            pos = 0; 
            ws = doInsert(key, value, q.child0, h-1);
         }
         else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
            pos = 1;
            ws = doInsert(key, value, q.child1, h-1);
         }
         else {
            pos = 2; 
            ws = doInsert(key, value, q.child2, h-1);
         }

         if (ws != null) {
            if (ws.newNode != null) {
               // make ws.newNode child # pos + ws.offset of q

               int sz = copyOutChildren(q, ws.scratch);
               insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
               if (sz == 2) {
                  ws.newNode = null;
                  ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
               }
               else {
                  ws.newNode = new InternalNode();
                  ws.offset = 1;
                  resetChildren(q, ws.scratch, 0, 2);
                  resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
               }
            }
            else if (ws.guideChanged) {
               ws.guideChanged = resetGuide(q);
            }
         }

         return ws;
      }
   }


   static int copyOutChildren(InternalNode q, Node[] x) {
   // copy children of q into x, and return # of children

      int sz = 2;
      x[0] = q.child0; x[1] = q.child1;
      if (q.child2 != null) {
         x[2] = q.child2; 
         sz = 3;
      }
      return sz;
   }

   static void insertNode(Node[] x, Node p, int sz, int pos) {
   // insert p in x[0..sz) at position pos,
   // moving existing extries to the right

      for (int i = sz; i > pos; i--)
         x[i] = x[i-1];

      x[pos] = p;
   }

   static boolean resetGuide(InternalNode q) {
   // reset q.guide, and return true if it changes.

      String oldGuide = q.guide;
      if (q.child2 != null)
         q.guide = q.child2.guide;
      else
         q.guide = q.child1.guide;

      return q.guide != oldGuide;
   }


   static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
   // reset q's children to x[pos..pos+sz), where sz is 2 or 3.
   // also resets guide, and returns the result of that

      q.child0 = x[pos]; 
      q.child1 = x[pos+1];

      if (sz == 3) 
         q.child2 = x[pos+2];
      else
         q.child2 = null;

      return resetGuide(q);
   }
}


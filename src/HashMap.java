public class HashMap {

    Vertex[] table;
    int totalCollisions;

    public HashMap(int s){
        // table size should be a prime number and 1/3 extra.
        table = new Vertex[s+(s/3)];
        totalCollisions = 0;
    }
    public int VertexToInt(Vertex key){

        int intVal = 0;
        String s = key.getCity().getName();
        for (int i = 0; i < s.length(); i++)
            intVal += s.charAt(i);

        return intVal;

    }

    public int Hash(int sum){
        return sum % table.length;
    }
    public int Rehash(int sum, int i){
        // first test linear probing on whole dictionary words, then Quadratic probing and
        // understand the changes in number of collision in each method
        return ((sum % table.length) + i) % table.length;
    }

    public void insert(Vertex v){

        int sum = VertexToInt(v);
        sum = Hash(sum);
        int collisions = 0;

        if (table[sum] == null) {
            table[sum] = v;
        } else {

            // linear probing:

            int i = 1;
            int newSum;
            do {
                newSum = Rehash(sum, i);
                collisions++;
                i++;
            } while (table[newSum] != null);
            table[newSum] = v;

        }

        totalCollisions += collisions;
    }
    public int search(Vertex v) {

        int sum = VertexToInt(v);
        sum = Hash(sum);

        // linear probing:

        if (table[sum].equals(v))
            return sum;
        else {

            int i = 1;
            int newSum;
            do {
                newSum = Rehash(sum, i);
                if (table[newSum].equals(v))
                    return newSum;
                i++;
            } while (table[newSum] != null && i < table.length);

        }

        System.out.println("word not found!");
        return -1;
    }

    public String displayTable() {

        String s = "\tindex\t|\telement\n";

        int i = 0;
        for (Vertex v : table) {
            s += "\t" + i + "\t\t|\t" + v + "\n";
            i++;
        }

        return s;

    }

}

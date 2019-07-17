package com.example.metroapp;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText et_from, et_to;
    Button btn_route;
    ListView listView;

    ArrayList<Station> stationArrayList = new ArrayList<>(5);
    ArrayAdapter<String> adapterString;
    ArrayAdapter<Station> adapterStation;
    Station[] stationArray = new Station[5];

    // a string array of stations in route
    String[] routeNameArray;
    int[] routeNodeArray;
    
    Graph metroMap = new Graph(35);

    String[] stationNameList = new String[]{"Netaji Subhash Place", "Shalimar Bagh", "Azadpur", "Model Town", "G.T.B. Nagar", "Vishwavidyalaya", "Vidhan Sabha", "Civil Lines", "Kashmere Gate", "Tis Hazari", "Pulbangash", "Pratap Nagar", "Shastri Nagar", "Inderlok", "Kanhaiya Nagar", "Keshav Puram", "Chandni Chowk", "Chawri Bazar", "New Delhi", "Rajiv Chowk", "RK Ashram Marg", "Jhandewalan", "Karol Bagh", "Rajendra Place", "Patel Nagar", "Shadipur", "Kirti Nagar", "Satguru Ram Singh Marg", "Ashok Park Main", "Moti Nagar", "Ramesh Nagar", "Rajouri Garden", "ESI - Basaidarapur", "Punjabi Bagh (W)", "Shakurpur"};
    int[] stationNodeList = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34};
    int[] stationColorCode = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
    boolean sourceEntered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et_from = findViewById(R.id.et_from);
        et_to = findViewById(R.id.et_to);
        btn_route = findViewById(R.id.btn_route);
        listView = findViewById(R.id.listView);

        Station[] stationArray = new Station[35];
        for(int i = 0; i<35; i++){
            stationArray[i] = new Station(stationNameList[i], stationNodeList[i], stationColorCode[i]);
        }

        // addEdges to the graph: metroMap
        addEdgesToMetroMap();

        // adding stations to the stationArrayList
        fillStationArrayList(stationArray);

        // populating the list view of all the stations
        populateListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!sourceEntered){
                    et_from.setText(stationArrayList.get(position).getStationName());
                    sourceEntered = true;
                } else {
                    et_to.setText(stationArrayList.get(position).getStationName());
                }
            }
        });

        btn_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = et_from.getText().toString();
                String to = et_to.getText().toString();

                if(from.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter boarding station", Toast.LENGTH_SHORT).show();
                    et_from.requestFocus();
                } else if(to.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter destination station", Toast.LENGTH_SHORT).show();
                    et_to.requestFocus();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Searching...", Toast.LENGTH_SHORT).show();

                    showPath(from, to);
//                    Intent i = new Intent(getApplicationContext(), RouteActivity.class);
//                    i.putExtra("nameList", routeNameArray);
//                    i.putExtra("nodeList", routeNodeArray);
//                    startActivity(i);
                }
            }
        });
    }



    // functions

    public void fillStationArrayList(Station stationArray[]){
        for(int i = 0; i < stationArray.length; i++){
            stationArrayList.add(stationArray[i]);
        }
    }

    public void fillStationArray(){
        for (int i = 0; i < stationArray.length; i++){
            stationArray[i].addStationValues(stationNameList[i], stationNodeList[i], stationColorCode[i]);
        }
    }

    public void populateListView(){
        adapterString = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stationNameList);
        listView.setAdapter(adapterString);
    }

    public void populateListViewRoute(){
        MyAdapter adapter = new MyAdapter(this, stationNameList, stationNodeList);
        listView.setAdapter(adapter);
    }

    public void addEdgesToMetroMap(){

        // Netaji Subhash Place
        metroMap.addEdge(0, 34);
        metroMap.addEdge(0, 15);
        metroMap.addEdge(0, 1);

        // Shalimar Bagh
        metroMap.addEdge(1, 0);
        metroMap.addEdge(1, 2);

        // Azadpur
        metroMap.addEdge(2, 1);
        metroMap.addEdge(2, 3);

        // Model Town
        metroMap.addEdge(3, 2);
        metroMap.addEdge(3, 4);

        // G.T.B. Nagar
        metroMap.addEdge(4, 3);
        metroMap.addEdge(4, 5);

        // Vishwavidyalaya
        metroMap.addEdge(5, 4);
        metroMap.addEdge(5, 6);

        // Vidhan Sabha
        metroMap.addEdge(6, 5);
        metroMap.addEdge(6, 7);

        // Civil Lines
        metroMap.addEdge(7, 6);
        metroMap.addEdge(7, 8);

        // Kashmere Gate
        metroMap.addEdge(8, 7);
        metroMap.addEdge(8, 9);

        // Tis Hazari
        metroMap.addEdge(9, 8);
        metroMap.addEdge(9, 10);

        // Pulbangash
        metroMap.addEdge(10, 9);
        metroMap.addEdge(10, 11);

        // Pratap Nagar
        metroMap.addEdge(11, 10);
        metroMap.addEdge(11, 12);

        // Shastri Nagar
        metroMap.addEdge(12, 11);
        metroMap.addEdge(12, 13);

        // Inderlok
        metroMap.addEdge(13, 12);
        metroMap.addEdge(13, 14);
        metroMap.addEdge(13, 28);

        // Kanhaiya Nagar
        metroMap.addEdge(14, 13);
        metroMap.addEdge(14, 15);

        // Keshav Puram
        metroMap.addEdge(15, 14);
        metroMap.addEdge(15, 0);

        // Chandni Chowk
        metroMap.addEdge(16, 8);
        metroMap.addEdge(16, 17);

        // Chawri Bazar
        metroMap.addEdge(17, 16);
        metroMap.addEdge(17, 18);

        // New Delhi
        metroMap.addEdge(18, 17);
        metroMap.addEdge(18, 19);

        // Rajiv Chowk
        metroMap.addEdge(19, 18);
        metroMap.addEdge(19, 20);

        // RK Ashram Marg
        metroMap.addEdge(20, 19);
        metroMap.addEdge(20, 21);

        // Jhandewalan
        metroMap.addEdge(21, 20);
        metroMap.addEdge(21, 22);

        // Karol Bagh
        metroMap.addEdge(22, 21);
        metroMap.addEdge(22, 23);

        // Rajendra Place
        metroMap.addEdge(23, 22);
        metroMap.addEdge(23, 24);

        // Patel Nagar
        metroMap.addEdge(24, 23);
        metroMap.addEdge(24, 25);

        // Shadipur
        metroMap.addEdge(25, 24);
        metroMap.addEdge(25, 26);

        // Kirti Nagar
        metroMap.addEdge(26, 25);
        metroMap.addEdge(26, 27);

        // Satguru Ram Singh Marg
        metroMap.addEdge(27, 26);
        metroMap.addEdge(27, 28);

        // Ashok Park Main
        metroMap.addEdge(28, 27);
        metroMap.addEdge(28, 29);

        // Moti Nagar
        metroMap.addEdge(29, 26);
        metroMap.addEdge(29, 30);

        // Ramesh Nagar
        metroMap.addEdge(30, 29);
        metroMap.addEdge(30, 31);

        // Rajouri Garden
        metroMap.addEdge(31, 30);
        metroMap.addEdge(31, 32);

        // ESI - Basaidarapur
        metroMap.addEdge(32, 31);
        metroMap.addEdge(32, 33);

        // Punjabi Bagh (W)
        metroMap.addEdge(33, 32);
        metroMap.addEdge(33, 34);

        // Shakurpur
        metroMap.addEdge(34, 33);
        metroMap.addEdge(34, 0);

    }

    public void showPath(String source, String destination) {
        int src, dest;
        src = search(source);
        dest = search(destination);

        // use something like: metroMap.get(position).getStationGraphNode;
    	metroMap.printAllPaths(src, dest);
    	ArrayList <ArrayList <Integer> > allRouteArrayList = new ArrayList <ArrayList<Integer>>();

    	allRouteArrayList = metroMap.getAllPaths();
    	
    	// say, selecting the first path only
    	ArrayList<Integer> routeArrayList = new ArrayList<>();
    	routeArrayList = allRouteArrayList.get(0);
    	int routeLength = routeArrayList.size();

    	// a string array of stations in route
    	routeNameArray = new  String[routeLength];
    	routeNodeArray = new int[routeLength];

    	routeNameArray = nodeToStation(routeArrayList, routeLength);
    	routeNodeArray = nodeArrayListToNodeArray(routeArrayList, routeLength);

    	// passing strings to intent
        Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
        intent.putExtra("nameList", routeNameArray);
        intent.putExtra("nodeList", routeNodeArray);
        startActivity(intent);

    }

    public int search(String s){
        int index=0;
        for(int i=0; i<stationNameList.length; i++)
            if(s == stationNameList[i])
                index = i;
        return index;
    }

    public String[] nodeToStation(ArrayList<Integer> route, int length){
    	String[] stationName = new String[length];
    	for(int i = 0; i < length; i++){
    		stationName[i] = stationNameList[route.get(i)];
    	}
    	return stationName;
    }

    public int[] nodeArrayListToNodeArray(ArrayList<Integer> route, int length){
    	int[] stationNode = new int[length];
    	for(int i=0; i<length; i++){
    		stationNode[i] = route.get(i);
    	}
    	return stationNode;
    }

    // creating a custom array adapter class

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String stationName[];
        int stationNode[];

        MyAdapter (Context c, String name[], int node[]) {
            super(c, R.layout.station_row, R.id.tv_stationName, name);
            this.context = c;
            this.stationName = name;
            this.stationNode = node;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.station_row, parent, false);
            TextView sName = row.findViewById(R.id.tv_stationName);
            TextView sNode = row.findViewById(R.id.tv_stationNode);

            // now set our resources on views
            sName.setText(stationName[position]);
            sNode.setText("Node number " + stationNode[position]);




            return row;
        }
    }
}

/*
 * Name:        Shaun Lee
 * Project:     Dijkstra's Algorithm Implementation 
 * Description: This program takes in 2 files and then implements dijkstra's algorithm to find the shortpath while giving the path
 * Date:        December 1st, 2023
 */

import java.io.*;
import java.util.*;

public class Dijkstras_Algorithm 
{   
    public static void main(String[] args) throws IOException
    {
        int matrix[][] = read_distance_matrix("distance_matrix.txt");

        List<String> airport = read_airport_codes("airport_codes.txt");

        Scanner sc = new Scanner(System.in);

        String start, end;
        boolean valid_start = false, valid_end = false;
        
        while (!valid_start || !valid_end) //loop when user inputs an airport code that doesn't exist from the text file
        {
            System.out.print("Start: ");
            start = sc.nextLine();

            if (airport.contains(start))
            {
                valid_start = true;
            }
            else
            {
                System.out.println("That airport code doesn't exist within the file. Please enter another code.");
            }

            System.out.println();
            System.out.print("End: ");
            end = sc.nextLine();

            if (airport.contains(end))
            {
                valid_end = true;
            }
            
            else
            {
                System.out.println("That airport code doesn't exist within the file. Please enter another code.");
            }

            if (valid_start && valid_end)
            {   
                System.out.println();
                calculate_dijkstras_algorithm(airport, matrix, start, end);
            }
        }

        System.out.println();
        sc.close();

    }

    private static final int infinity = 1000000; //used for setting default distance

    static int[][] read_distance_matrix(String file) throws IOException
    {
        List<String> lines = new ArrayList<>(); //Use this to store distance_matrix.txt data into an ArrayList
        String file_line; // Variable to read matrix data one line at a time
        BufferedReader reader = new BufferedReader(new FileReader(file)); //read in

        while ((file_line = reader.readLine()) != null) //reads file until no more lines
        {   
            if (file_line.trim().length() != 0)
            {
                lines.add(file_line);
            }
        }
        reader.close();

        int size = lines.size();

        int[][] distance_matrix = new int[size][size]; //sets dimensions of matrix to size of distance_matrix

        for (int i = 0; i < size; i++)
        {
            String[] distance = lines.get(i).split(" "); // getting rid of spaces
            for (int j = 0; j < size; j++)
            {
                distance_matrix[i][j] = Integer.parseInt(distance[j]);      
            }
        }
        return distance_matrix;
    }

    private static List<String> read_airport_codes(String file) throws IOException 
    {   
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        reader.close();

        return Arrays.asList(line.split(" ")); //returns an ArrayList<airport_codes>
    }
    
    public static void calculate_dijkstras_algorithm(List<String> airport_code, int[][] distance_matrix, String start, String end) 
    {
        int size = airport_code.size(); //setting size as 8
        int[] distances = new int[size]; 
        int[] predecessors = new int[size];
        boolean[] visited = new boolean[size];
    
        // Initialize distances to infinity and predecessors to 0
        Arrays.fill(distances, infinity);
        Arrays.fill(predecessors, 0);
    
        // Find the index of start and end vertices in the airport_code list
        int start_index = airport_code.indexOf(start); //grabs name of airport_code and index of it
        int end_index = airport_code.indexOf(end); //grabs name of airport_code and index of it
    
        // start airport has a distance of 0 from itself
        distances[start_index] = 0;
    
        PriorityQueue<Integer> unvisited_queue = new PriorityQueue<>(Comparator.comparingInt(airport -> distances[airport])); //assigning distance values to respective airport using Comparator and int values
    
        for (int i = 0; i < size; i++) //iterating through size of airport codes
        {
            unvisited_queue.offer(i); //adds the amount of vertices into the queue
        }
    
        while (!unvisited_queue.isEmpty()) //while the queue is not empty, keep doing this
        {
            int current_airport = unvisited_queue.poll(); //removes the current airport from the queue;

            visited[current_airport] = true; //sets current airport as visited
    
            for (int adjacent_airport = 0; adjacent_airport < size; adjacent_airport++) //initializes other vertices
            {
                //check if current airport of the matrix has an adjacent airport that is reachable and has not been visited.
                if (distance_matrix[current_airport][adjacent_airport] != infinity && !visited[adjacent_airport]) //have to invert visited so that the if statement turns to true and runs
                {   
                    //calculates the value between vertices and sets it as the edge weight
                    int edge_weight = distance_matrix[current_airport][adjacent_airport]; 

                    int alternate_path_distance = distances[current_airport] + edge_weight; // adds the edge weight to the current distance
    
                    if (alternate_path_distance < distances[adjacent_airport]) //if the alternate path is less than the distance of an adjacent airport
                    {
                        distances[adjacent_airport] = alternate_path_distance; //sets the index of the airport to the value of edge weight
                        predecessors[adjacent_airport] = current_airport; //sets predecessor of current index to the current airport
                        unvisited_queue.remove(adjacent_airport); //removes the index of the adjacent airport from the queue
                        unvisited_queue.offer(adjacent_airport); //adds the adjacent airport back into queue to compare edge weights 
                    }
                }
            }
        }
    
        System.out.println("Shortest route between " + start + " to " + end + " is: " + distances[end_index]);
    
        // print path from start to end
        String route = "";
        System.out.print("Path: ");
        int index = end_index; //starts from the bottom of the stack and works up so that the start is at the top of the queue and prints in order
        Stack<String> path = new Stack<>();

        while (index != start_index) 
        {
            path.push(airport_code.get(index)); //will keep pushing items until start index
            index = predecessors[index];
        }
        
        path.push(start);
        
        while (!path.isEmpty()) 
        {   
            route = route + path.pop();
            if (!path.isEmpty())
            {
                route = route + " -> ";
            }
        }
        System.out.println(route);
    }
    
}




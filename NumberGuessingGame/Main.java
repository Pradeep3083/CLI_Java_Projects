import java.util.Random;
import java.util.Scanner;

public class Main
{
  
	public static void main(String[] args) {
	    Random random = new Random();
	    Scanner sc = new Scanner(System.in);
	    
	    int guess;
	    int attempts = 0;
	    int min = 1;
	    int max = 10;
	    int randomNumber = random.nextInt(min, max);
	    
	    System.out.println("Number Guessing game");
	    System.out.printf("Guess a number between %d-%d:\n",min,max);
	    
	    do{
	        System.out.print("Enter a guess: ");
	        guess = sc.nextInt();
	        attempts++;
	        
	        if(guess < randomNumber){
	            System.out.println("TOO LOW! Try again...");
	        }else if(guess > randomNumber){
	            System.out.println("TOO HIGH! Try again...");
	        }else{
	            System.out.println("CORRECT! The number was "+randomNumber);
	            System.out.println("No. of Attempts: "+attempts);
	        }	        
	    }while(guess != randomNumber);
    
	}
}

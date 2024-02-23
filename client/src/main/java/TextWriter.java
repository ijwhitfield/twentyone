import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.EOFException;
import java.io.IOException;

public class TextWriter{
    private DefaultTerminalFactory termFac = new DefaultTerminalFactory();
    private int choice = -1;
    private String input = "";
    private Screen screen;
    private Window window = new BasicWindow();
    private Panel panel;
    private WindowBasedTextGUI tg;
    private Thread guiThread;
    final private TextBox inputTB = new TextBox();


    public TextWriter(){
        try {
            screen = termFac.createScreen();
            panel = new Panel(new LinearLayout());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void start(){
        tg = new MultiWindowTextGUI(screen);
        try{
            screen.startScreen();
        }catch(IOException e){
            e.printStackTrace();
        }
        tg.addWindow(window);
        window.setComponent(panel);
        guiThread = new Thread(new Runnable(){
            public void run(){
                guiThreadProcess();
            }
        });
        guiThread.start();
    }

    private void guiThreadProcess(){ //infinite loop, must be threaded
        while(window.isVisible()){ //this is the gui process, limited to 10 fps because it was killing my cpu
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            try{
                tg.processInput();
                tg.updateScreen();
            } catch(EOFException e){
                break;
            } catch(IOException e){
                e.printStackTrace();
            } 
        }
        System.exit(0);
        
    }

    public void askInput(){
        //add textbox
        //also add Done button which does setInput
        inputTB.setText("");
        input="";
        panel.addComponent(inputTB);
        panel.addComponent(new Button("Done", new Runnable(){
            public void run(){
                if(!inputTB.getText().equals("")){
                    setInput(inputTB.getText());
                }
            }
        }));
        window.setFocusedInteractable(panel.nextFocus(null));
    }

    public void setInput(String input){
        this.input=input;
    }

    public String getInput(){
        while(input.equals("")){
            //sleeps just to reduce the while spam
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return input;
    }
    
    /*public void askChoice(String[] buttons){
        choice = -1;
        for(int i = 0; i<buttons.length; i++){
            //add button with that string as a value
            panel.addComponent(new Button("Done", new Runnable(){
                public void run(){
                    if(!inputTB.getText().equals("")){
                        setChoice(i);
                    }
                }
            }));
        }
        window.setFocusedInteractable(panel.nextFocus(null));

    }*/
    public void askChoice(){
        choice = -1;
        panel.addComponent(new Button("Hit",new Runnable(){
            public void run(){
                setChoice(0);
            }
        }));
        panel.addComponent(new Button("Stay",new Runnable(){
            public void run(){
                setChoice(1);
            }
        }));
        window.setFocusedInteractable(panel.nextFocus(null));
    }

    public void setChoice(int choice){
        this.choice = choice;
    }

    public int getChoice(){
        //wait until choice is not -1
        //guithread will set choice when a button is pressed, so dont worry
        while(choice==-1){
            //sleeps just to reduce the while spam
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return choice;
    }
    public void clear(){
        panel.removeAllComponents();
    }
    public void write(String input){
        panel.addComponent(new Label(input));
    }
    public void stop(){
        if(screen != null){
            try{
                screen.stopScreen();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            
        }
    }
}
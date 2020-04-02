package shlomi.window;

public class StatusPanel extends MyGuiComps.MyPanel {

    MyGuiComps.MyTextArea textArea;
    MyGuiComps.MyScrollPane scrollPane;

    public StatusPanel() {
        initizlize();
    }

    private void initizlize() {

        // Text area
        textArea = new MyGuiComps.MyTextArea();

        // Scroll pane
        scrollPane = new MyGuiComps.MyScrollPane( textArea );
//        scrollPane.setBounds( 5, 25, getWidth() - , );
        add( scrollPane );

    }
}

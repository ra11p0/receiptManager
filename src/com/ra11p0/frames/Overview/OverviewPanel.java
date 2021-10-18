package com.ra11p0.frames.Overview;

import com.ra11p0.frames.Overview.structures.Day;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.frames.ReceiptsManager.StoreSelector;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class OverviewPanel extends JPanel {
    private final ArrayList<Day> days = new ArrayList<>();
    private JPanel content = new JPanel(new BorderLayout());
    private JPanel navigation = new JPanel(new BorderLayout());
    private JPanel data = new JPanel(new GridLayout(2, 5));
    private final JLabel aTax = new JLabel("0", SwingConstants.CENTER);
    private final JLabel bTax = new JLabel("0", SwingConstants.CENTER);
    private final JLabel cTax = new JLabel("0", SwingConstants.CENTER);
    private final JLabel noTax = new JLabel("0", SwingConstants.CENTER);
    private final JLabel total = new JLabel("0", SwingConstants.CENTER);
    public OverviewPanel(){
        setLayout(new BorderLayout());
        setSize(800, 650);
        generateDaySet(new Date(System.currentTimeMillis()-(6*24 * 60 * 60 * 1000)), new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
        generateDataPanel();
        generateNavigationPanel();
        generateContentPanel();
    }
    private void generateNavigationPanel(){
        navigation.setVisible(false);
        navigation.removeAll();
        navigation = new JPanel(new BorderLayout());
        add(navigation, BorderLayout.PAGE_START);
        //*****
        Button previous = new Button("Previous");
        previous.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Date from = new Date(days.get(0).get_date().getTime()-(7*24 * 60 * 60 * 1000));
                Date to = new Date(days.get(0).get_date().getTime());
                generateDaySet(from, to);
                generateNavigationPanel();
                generateContentPanel();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(days.get(0).get_date().getTime());
        JLabel month = new JLabel(new SimpleDateFormat("MMMMMMMMMMM").format(calendar.getTime()), SwingConstants.CENTER);
        Button next = new Button("Next");
        next.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Date to = new Date(days.get(6).get_date().getTime() + (8*24 * 60 * 60 * 1000));
                Date from = new Date(days.get(6).get_date().getTime() + (24 * 60 * 60 * 1000));
                generateDaySet(from, to);
                generateNavigationPanel();
                generateContentPanel();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        navigation.add(previous, BorderLayout.LINE_START);
        navigation.add(month, BorderLayout.CENTER);
        navigation.add(next, BorderLayout.LINE_END);
        navigation.setVisible(true);
    }
    private void generateContentPanel() {
        //*****
        final int daysPerView = 7;
        //*****
        content.setVisible(false);
        content = new JPanel(new BorderLayout());
        add(content, BorderLayout.CENTER);
        //*****
        JPanel dates = new JPanel(new GridLayout(1, daysPerView));
        for (Day day : days) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            calendar.setTimeInMillis(day.get_date().getTime());
            dates.add(new JLabel(calendar.get(Calendar.MONTH) + 1 + " - " + calendar.get(Calendar.DAY_OF_MONTH), SwingConstants.CENTER));
        }
        content.add(dates, BorderLayout.PAGE_START);
        //*****
        JPanel receiptsPanel = new JPanel(new GridLayout(1, daysPerView));
        ArrayList<JLabel> totalValues = new ArrayList<>();
        ArrayList<DefaultListModel<Receipt>> receiptListModels = new ArrayList<>();
        ArrayList<JList<Receipt>> receiptListArray = new ArrayList<>();
        ArrayList<JPanel> jPanels = new ArrayList<>();
        for (int i = 0; i < daysPerView; i++) {
            jPanels.add(new JPanel(new BorderLayout()));
            receiptsPanel.add(jPanels.get(jPanels.size() - 1));
        }
        for (JPanel panel : jPanels) {
            JList<Receipt> jList = new JList<>();
            receiptListArray.add(jList);
            JScrollPane scrollPane = new JScrollPane(jList);
            jList.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    for(JList<Receipt> receiptJListItem : receiptListArray){
                        if(!receiptJListItem.equals(jList)) receiptJListItem.clearSelection();
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                }
            });
            totalValues.add(new JLabel(String.format("%.2f", days.get(receiptListModels.size()).get_total()) + "PLN", SwingConstants.CENTER));
            receiptListModels.add(new DefaultListModel<>());
            jList.setModel(receiptListModels.get(receiptListModels.size() - 1));
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(totalValues.get(totalValues.size() - 1), BorderLayout.PAGE_END);
        }
        for (Day day : days)
            if (day.get_receipts() != null)
                for (Receipt receipt : day.get_receipts())
                    receiptListModels.get(days.indexOf(day)).addElement(receipt);
        content.add(receiptsPanel, BorderLayout.CENTER);
        //*****
        JPanel controlButtons = new JPanel(new GridLayout(1, 4));
        Button edit = new Button("Edit");
        edit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for(JList<Receipt> jList : receiptListArray){
                    if(jList.getSelectedValue()!=null){
                        setVisible(false);
                        data.removeAll();
                        content.removeAll();
                        content.add(new EditorPanel(jList.getSelectedValue(), ReceiptsManager.getItems()), BorderLayout.CENTER);
                        Button backButton = new Button("Back");
                        backButton.addMouseListener(new MouseListener() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                generateContentPanel();
                                generateNavigationPanel();
                                generateDataPanel();
                            }

                            @Override
                            public void mousePressed(MouseEvent e) {

                            }

                            @Override
                            public void mouseReleased(MouseEvent e) {

                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {

                            }

                            @Override
                            public void mouseExited(MouseEvent e) {

                            }
                        });
                        navigation.removeAll();
                        navigation.add(backButton, BorderLayout.PAGE_START);
                        setVisible(true);
                        break;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Button newReceipt = new Button("New receipt");
        newReceipt.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame temp = StoreSelector.showDialog(ReceiptsManager.getItems());
                temp.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        //super.windowClosing(e);
                        generateDaySet(days.get(0).get_date(), new Date(days.get(0).get_date().getTime() + (7 * 24 * 60 * 60 * 1000)));
                        generateNavigationPanel();
                        generateContentPanel();
                    }
                });
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Button remove = new Button("Remove");
        remove.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for(JList<Receipt> jList : receiptListArray){
                    if(jList.getSelectedValue()!=null){
                        jList.getSelectedValue().deleteReceipt();
                        generateDaySet(days.get(0).get_date(), new Date(days.get(0).get_date().getTime() + (7 * 24 * 60 * 60 * 1000)));
                        generateNavigationPanel();
                        generateContentPanel();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Button stats = new Button("Statistics");
        controlButtons.add(edit);
        controlButtons.add(newReceipt);
        controlButtons.add(remove);
        controlButtons.add(stats);
        content.add(controlButtons, BorderLayout.PAGE_END);
        content.setVisible(true);
    }
    private void generateDataPanel(){
        data.removeAll();
        data = new JPanel(new GridLayout(2, 5));
        add(data, BorderLayout.PAGE_END);
        //*****
        data.add(new JLabel("23% Tax", SwingConstants.CENTER));
        data.add(new JLabel("8% Tax", SwingConstants.CENTER));
        data.add(new JLabel("5% Tax", SwingConstants.CENTER));
        data.add(new JLabel("0% Tax", SwingConstants.CENTER));
        data.add(new JLabel("Total", SwingConstants.CENTER));
        data.add(aTax);
        data.add(bTax);
        data.add(cTax);
        data.add(noTax);
        data.add(total);
    }
    private void generateDaySet(Date from, Date to){
        float aTaxValue = 0;
        float bTaxValue = 0;
        float cTaxValue = 0;
        float noTaxValue = 0;
        float totalValue = 0;
        days.clear();
        long diffInMs = Math.abs(to.getTime() - from.getTime());
        int dayCount = (int)TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);
        for(int i=0; i<dayCount; i++){
            Date currentlyProcessedDate = new Date((from.getTime() + ((long) (i) * 24 * 60 * 60 * 1000)));
            ArrayList<Receipt> currentDayReceipts = new ArrayList<>();
            for(Receipt receipt : ReceiptsManager.getReceipts()) {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(currentlyProcessedDate);
                cal2.setTime(receipt.get_date());
                boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
                if (sameDay) {
                    currentDayReceipts.add(receipt);
                }
            }
            if (currentDayReceipts.size()!=0) days.add(new Day(currentlyProcessedDate, currentDayReceipts));
            else days.add(new Day(currentlyProcessedDate, null));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(days.get(0).get_date());
        for(Receipt receipt : ReceiptsManager.getReceipts()){
            Calendar receiptCalendar = Calendar.getInstance();
            receiptCalendar.setTime(receipt.get_date());
            if(receiptCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)){
                totalValue+= receipt.get_paid();
                for(ReceiptItem receiptItem : receipt.get_items()){
                    if(receiptItem.get_Item().get_taxRate() == 0.23F) aTaxValue += receiptItem.get_qty() * receiptItem.get_Item().get_price();
                    if(receiptItem.get_Item().get_taxRate() == 0.08F) bTaxValue += receiptItem.get_qty() * receiptItem.get_Item().get_price();
                    if(receiptItem.get_Item().get_taxRate() == 0.05F) cTaxValue += receiptItem.get_qty() * receiptItem.get_Item().get_price();
                    if(receiptItem.get_Item().get_taxRate() == 0F) noTaxValue += receiptItem.get_qty() * receiptItem.get_Item().get_price();
                }
            }
        }
        aTax.setText(String.format("%.2f", aTaxValue) + " PLN");
        bTax.setText(String.format("%.2f", bTaxValue) + " PLN");
        cTax.setText(String.format("%.2f", cTaxValue) + " PLN");
        noTax.setText(String.format("%.2f", noTaxValue) + " PLN");
        total.setText(String.format("%.2f", totalValue) + " PLN");
    }
}

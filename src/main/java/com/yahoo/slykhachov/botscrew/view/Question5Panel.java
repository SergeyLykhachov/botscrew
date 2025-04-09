package com.yahoo.slykhachov.botscrew.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yahoo.slykhachov.botscrew.model.Model;
import com.yahoo.slykhachov.botscrew.util.Updatable;

public class Question5Panel extends JPanel implements Updatable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Question5Panel.class);
    private final AnswerPanel answerPanel;
    private String template;

    Question5Panel(Model model) {
        this.answerPanel = new AnswerPanel();
        this.add(new InquiryPanel(model, this), BorderLayout.NORTH);
        this.add(answerPanel, BorderLayout.CENTER);
    }

    private void setTemplate(String template) {
        this.template = template;
    }

    private String getTemplate() {
        return this.template;
    }

    private static class InquiryPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        static final String question;
        static final String sqlTemplate;
        static {
            question = "Global search by ";
            sqlTemplate = "SELECT first_name, last_name "
                + "FROM lector "
                + "WHERE first_name LIKE ? OR last_name LIKE ?";
        }
        private final Model model;
        private final JTextField textField;

        InquiryPanel(Model model, Question5Panel q5p) {
            this.model = model;
            JLabel label = new JLabel();
            label.setText(InquiryPanel.question);
            this.textField = new JTextField(20);
            JButton button = new JButton("Query");
            button.addActionListener(
                e -> {
                    q5p.setTemplate(this.textField.getText());
                    this.textField.setText("");
                    this.model.executeQuery(
                        InquiryPanel.sqlTemplate,
                        "%" + q5p.getTemplate() + "%",
                        "%" + q5p.getTemplate() + "%"
                    );
                });
            this.add(label);
            this.add(textField);
            this.add(button);
        }
    }

    private static class AnswerPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private DefaultListModel<String> nameListModel;
        AnswerPanel() {
            JLabel label = new JLabel();
            label.setText("Answer:");
            this.add(label, BorderLayout.WEST);
            JPanel listContainer = new JPanel();
            JList<String> nameList = new JList<>();
            nameList.setFixedCellWidth(450);
            nameList.setFixedCellHeight(20);
            this.nameListModel = new DefaultListModel<>();
            nameList.setModel(nameListModel);
            listContainer.setLayout(new BorderLayout());
            listContainer.add(new JScrollPane(nameList), BorderLayout.CENTER);
            this.add(listContainer, BorderLayout.CENTER);
        }

        void setAnswer(List<String> list) {
            this.nameListModel.clear();
            list.forEach(this.nameListModel::addElement);
        }
    }

    @Override
    public void post(ResultSet resultSet) {
        List<String> list = new ArrayList<>();
        String name;
        try {
            while (resultSet.next()) {
                name = resultSet.getString(1);
                name += " ";
                name += resultSet.getString(2);
                list.add(name);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        this.answerPanel.setAnswer(list);
    }
}


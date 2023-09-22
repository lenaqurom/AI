import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;

public class AI extends JFrame {
    private JPanel homePanel;
    private JButton binaryButton;
    private JButton multiButton;

    public AI() {
        // Set up the main frame
        setTitle("Home Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create the home panel with white background
        homePanel = new JPanel();
        homePanel.setBackground(Color.WHITE);
        homePanel.setLayout(new GridBagLayout());

     // Create the binary button
        binaryButton = createButton("Binary", Color.BLACK);
        binaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Binary Frame");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new BinaryFrame());
                frame.pack();
                frame.setVisible(true);
                frame.setSize(800, 600);

                dispose();
            }
        });

        // Create the multi button
        multiButton = createButton("Multi", Color.BLACK);
        multiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Multi Frame");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new MultiFrame());
                frame.pack();
                frame.setVisible(true);
                frame.setSize(800, 600);

                dispose();
            }
        });

        // Add the buttons to the home panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        homePanel.add(binaryButton, gbc);

        gbc.gridy = 1;
        homePanel.add(multiButton, gbc);

        // Add the home panel to the main frame
        add(homePanel);
    }

    private JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 50));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AI().setVisible(true);
            }
        });
    }
}
 class MultiFrame extends JPanel {
    private List<DataPoint> dataPoints;
    private SoftmaxClassifier classifier;

    // GUI components
    private JLabel dataPointLabel;
    private JTextField xField;
    private JTextField yField;
    private JComboBox<String> classComboBox;
    private JLabel learningRateLabel;
    private JTextField learningRateField;
    private JLabel maxIterationsLabel;
    private JTextField maxIterationsField;
    private JButton addButton;
    private JButton trainButton;
    private JButton homeButton;
    private JLabel accuracyLabel;


    public MultiFrame() {
        dataPoints = new ArrayList<>();
        classifier = new SoftmaxClassifier(2, 4, 0.1, 1000); // Set the default values for the classifier

        // Create GUI components
        dataPointLabel = new JLabel("Data Point:");

        xField = new JTextField(10);
        yField = new JTextField(10);
        classComboBox = new JComboBox<>(new String[]{"Class 0", "Class 1", "Class 2", "Class 3"});
        learningRateLabel = new JLabel("Learning rate:");
        learningRateField = new JTextField(10);
        maxIterationsLabel = new JLabel("Max num of Iterations:");
        maxIterationsField = new JTextField(10);
        addButton = new JButton("Add");
        trainButton = new JButton("Train");
        homeButton = new JButton("Home");

        // Set button and label colors
        Color buttonColor = Color.BLACK;
        Color fontColor = Color.WHITE;

        addButton.setBackground(buttonColor);
        addButton.setForeground(fontColor);
        trainButton.setBackground(buttonColor);
        trainButton.setForeground(fontColor);
        homeButton.setBackground(buttonColor);
        homeButton.setForeground(fontColor);

        // Set font color for labels
        dataPointLabel.setForeground(Color.BLACK);
        learningRateLabel.setForeground(Color.BLACK);
        maxIterationsLabel.setForeground(Color.BLACK);
        

        // Set font for buttons and labels
        Font buttonFont = new Font("Script MT Bold", Font.BOLD, 18);
        Font labelFont = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 16);
        dataPointLabel.setFont(labelFont);
        learningRateLabel.setFont(labelFont);
        maxIterationsLabel.setFont(labelFont);
        addButton.setFont(buttonFont);
        trainButton.setFont(buttonFont);
        homeButton.setFont(buttonFont);

        accuracyLabel = new JLabel("Accuracy: ");
        accuracyLabel.setFont(labelFont);
        accuracyLabel.setForeground(Color.BLACK);


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDataPointFromFields();
                clearTextFields();
                repaint();
            }
        });
        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainClassifier();
                repaint();
                double accuracy = getAccuracy();
                accuracyLabel.setText("Accuracy: " + (accuracy * 100) + "%");
            }
        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AI().setVisible(true);
            }

        });

        // Add components to the panel
        add(dataPointLabel);
        add(xField);
        add(yField);
        add(classComboBox);
        add(learningRateLabel);
        add(learningRateField);
        add(maxIterationsLabel);
        add(maxIterationsField);
        add(addButton);
        add(trainButton);
        add(homeButton);
        add(accuracyLabel);

    }

    private void addDataPointFromFields() {
        double x = Double.parseDouble(xField.getText());
        double y = Double.parseDouble(yField.getText());
        int trueClass = classComboBox.getSelectedIndex();
        dataPoints.add(new DataPoint(x, y, trueClass));
    }

    private void clearTextFields() {
        xField.setText("");
        yField.setText("");
    }

    public void trainClassifier() {
        double learningRate = Double.parseDouble(learningRateField.getText());
        int maxIterations = Integer.parseInt(maxIterationsField.getText());
        classifier.setLearningRate(learningRate);
        classifier.setMaxIterations(maxIterations);
        classifier.train(dataPoints);
    }

    public double getAccuracy() {
        int correctPredictions = 0;
        int totalDataPoints = dataPoints.size();

        for (DataPoint dataPoint : dataPoints) {
            double[] features = new double[3];
            features[0] = 1.0;
            features[1] = dataPoint.getX();
            features[2] = dataPoint.getY();

            int trueClass = dataPoint.getTrueClass();
            int predictedClass = classifier.predictClass(features);

            if (predictedClass == trueClass) {
                correctPredictions++;
            }
        }

        double accuracy = (double) correctPredictions / totalDataPoints;
        return accuracy;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set background color to white
        setBackground(Color.WHITE);

        // Draw the x and y axes
        int width = getWidth();
        int height = getHeight();
        int margin = 40;

        g.setColor(Color.BLACK);
        g.drawLine(margin, height - margin, width - margin, height - margin); // x-axis
        g.drawLine(margin, margin, margin, height - margin); // y-axis

        // Draw the decision boundaries
        g.setColor(Color.RED);
        double[][] weights = classifier.getWeights();
        if (weights.length > 0) {
            int numClasses = weights.length;
            int numFeatures = weights[0].length - 1;

            for (int i = 0; i < numClasses; i++) {
                double x1 = 0;
                double y1 = (-weights[i][0] - weights[i][1] * x1) / weights[i][2];
                double x2 = 10;
                double y2 = (-weights[i][0] - weights[i][1] * x2) / weights[i][2];

                int px1 = margin + (int) (x1 * (width - 2 * margin) / 10.0);
                int py1 = height - margin - (int) (y1 * (height - 2 * margin) / 10.0);
                int px2 = margin + (int) (x2 * (width - 2 * margin) / 10.0);
                int py2 = height - margin - (int) (y2 * (height - 2 * margin) / 10.0);

                g.drawLine(px1, py1, px2, py2);
            }
        }

        // Draw the data points
        for (DataPoint dataPoint : dataPoints) {
            int x = margin + (int) (dataPoint.getX() * (width - 2 * margin) / 10.0);
            int y = height - margin - (int) (dataPoint.getY() * (height - 2 * margin) / 10.0);
            Color color = getColorForClass(dataPoint.getTrueClass());
            g.setColor(color);
            g.fillOval(x - 5, y - 5, 10, 10);
        }
    }

    private Color getColorForClass(int trueClass) {
        switch (trueClass) {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLACK;
            default:
                return Color.GRAY;
        }
    }

}
class BinaryFrame extends JPanel {
    private List<DataPoint> dataPoints;
    private LogisticRegressionClassifier classifier;

 // GUI components
    private JLabel dataPointLabel;
    private JTextField xField;
    private JTextField yField;
    private JComboBox<String> classComboBox;
    private JLabel learningRateLabel;
    private JTextField learningRateField;
    private JLabel maxIterationsLabel;
    private JTextField maxIterationsField;
    private JButton addButton;
    private JButton trainButton;
    private JButton homeButton;
    private JLabel accuracyLabel;
    
    public BinaryFrame() {
        dataPoints = new ArrayList<>();
        classifier = new LogisticRegressionClassifier(2, 0.1, 1000); // Set the default values for the classifier
        
        xField = new JTextField(10);
        yField = new JTextField(10);
        classComboBox = new JComboBox<>(new String[]{"Class 0", "Class 1"});
        learningRateLabel = new JLabel("Learning rate:");
        learningRateField = new JTextField(10);
        maxIterationsLabel = new JLabel("Max num of Iterations:");
        maxIterationsField = new JTextField(10);
        addButton = new JButton("Add");
        trainButton = new JButton("Train");
        homeButton = new JButton("Home");

        // Set button and label colors
        Color buttonColor = Color.BLACK;
        Color fontColor = Color.WHITE;

        addButton.setBackground(buttonColor);
        addButton.setForeground(fontColor);
        trainButton.setBackground(buttonColor);
        trainButton.setForeground(fontColor);
        homeButton.setBackground(buttonColor);
        homeButton.setForeground(fontColor);

        // Set font color for labels
        dataPointLabel = new JLabel();
        dataPointLabel.setForeground(Color.BLACK);
        learningRateLabel.setForeground(Color.BLACK);
        maxIterationsLabel.setForeground(Color.BLACK);
        

        // Set font for buttons and labels
        Font buttonFont = new Font("Script MT Bold", Font.BOLD, 18);
        Font labelFont = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 16);
        dataPointLabel.setFont(labelFont);
        learningRateLabel.setFont(labelFont);
        maxIterationsLabel.setFont(labelFont);
        addButton.setFont(buttonFont);
        trainButton.setFont(buttonFont);
        homeButton.setFont(buttonFont);

        accuracyLabel = new JLabel("Accuracy: ");
        accuracyLabel.setFont(labelFont);
        accuracyLabel.setForeground(Color.BLACK);


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDataPointFromFields();
                clearTextFields();
                repaint();
            }
        });
        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainClassifier();
                repaint();
                double accuracy = getAccuracy();
                accuracyLabel.setText("Accuracy: " + accuracy);
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AI().setVisible(true);
            }

        });

        // Add components to the panel
        add(dataPointLabel);
        add(xField);
        add(yField);
        add(classComboBox);
        add(learningRateLabel);
        add(learningRateField);
        add(maxIterationsLabel);
        add(maxIterationsField);
        add(addButton);
        add(trainButton);
        add(homeButton);
        add(accuracyLabel);

        }

    public void addDataPointFromFields() {
    	double x = Double.parseDouble(xField.getText());
        double y = Double.parseDouble(yField.getText());
        int trueClass = classComboBox.getSelectedIndex();
        dataPoints.add(new DataPoint(x, y, trueClass));
    }

    private void clearTextFields() {
        xField.setText("");
        yField.setText("");
    }


    public void trainClassifier() {
   	 double learningRate = Double.parseDouble(learningRateField.getText());
     int maxIterations = Integer.parseInt(maxIterationsField.getText());
     classifier.setLearningRate(learningRate);
     classifier.setMaxIterations(maxIterations);
     classifier.train(dataPoints);
    }

    public double getAccuracy() {
        int correctPredictions = 0;
        int totalDataPoints = dataPoints.size();

        for (DataPoint dataPoint : dataPoints) {
            double[] features = new double[2];
            features[0] = dataPoint.getX();
            features[1] = dataPoint.getY();

            int trueClass = dataPoint.getTrueClass();
            int predictedClass = classifier.predict(features);

            if (predictedClass == trueClass) {
                correctPredictions++;
            }
        }

        return (double) ((correctPredictions / totalDataPoints)*100); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set background color to white
        setBackground(Color.WHITE);

        // Draw the x and y axes
        int width = getWidth();
        int height = getHeight();
        int margin = 40;

        g.setColor(Color.BLACK);
        g.drawLine(margin, height - margin, width - margin, height - margin); // x-axis
        g.drawLine(margin, margin, margin, height - margin); // y-axis

        // Draw the decision boundary
        g.setColor(Color.RED);
        double[] weights = classifier.getWeights();

        if (weights.length > 0) {
            double w0 = weights[0];
            double w1 = weights[1];
            double w2 = weights[2];

            if (w2 != 0) {
                double x1 = 0;
                double y1 = (-w0 - w1 * x1) / w2;
                double x2 = 10;
                double y2 = (-w0 - w1 * x2) / w2;

                int px1 = margin + (int) (x1 * (width - 2 * margin) / 10.0);
                int py1 = height - margin - (int) (y1 * (height - 2 * margin) / 10.0);
                int px2 = margin + (int) (x2 * (width - 2 * margin) / 10.0);
                int py2 = height - margin - (int) (y2 * (height - 2 * margin) / 10.0);

                g.drawLine(px1, py1, px2, py2);
            }
        }

        // Draw the data points
        for (DataPoint dataPoint : dataPoints) {
            int x = margin + (int) (dataPoint.getX() * (width - 2 * margin) / 10.0);
            int y = height - margin - (int) (dataPoint.getY() * (height - 2 * margin) / 10.0);

            if (dataPoint.getTrueClass() == 0) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.GREEN);
            }

            g.fillOval(x - 5, y - 5, 10, 10);
        }
}
}
class DataPoint {
    private double x;
    private double y;
    private int trueClass;

    public DataPoint(double x, double y, int trueClass) {
        this.x = x;
        this.y = y;
        this.trueClass = trueClass;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTrueClass() {
        return trueClass;
    }
}

class LogisticRegressionClassifier {
    private double[] weights;
    private double learningRate;
    private int maxIterations;

    public LogisticRegressionClassifier(int numFeatures, double learningRate, int maxIterations) {
        weights = new double[numFeatures + 1]; // Add 1 for the bias term
        this.learningRate = learningRate;
        this.maxIterations = maxIterations;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public double[] getWeights() {
        return weights;
    }

    public int predict(double[] features) {
        double activation = calculateActivation(features);
        return (activation >= 0.5) ? 1 : 0;
    }

    private double calculateActivation(double[] features) {
        double activation = weights[0]; // Bias term

        for (int i = 0; i < features.length; i++) {
            activation += weights[i + 1] * features[i];
        }

        return sigmoid(activation);
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public void train(List<DataPoint> dataPoints) {
        int numFeatures = weights.length - 1;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            for (DataPoint dataPoint : dataPoints) {
                double[] features = new double[numFeatures];
                features[0] = dataPoint.getX();
                features[1] = dataPoint.getY();

                double activation = calculateActivation(features);
                double error = dataPoint.getTrueClass() - activation;

                // Update the weights
                weights[0] += learningRate * error; // Bias term

                for (int i = 0; i < numFeatures; i++) {
                    weights[i + 1] += learningRate * error * features[i];
                }
            }
        }
    }
}
class SoftmaxClassifier {
    private double[][] weights;
    private double learningRate;
    private int maxIterations;

    public SoftmaxClassifier(int numFeatures, int numClasses, double learningRate, int maxIterations) {
        this.weights = new double[numClasses][numFeatures + 1]; // Add one for the bias term
        this.learningRate = learningRate;
        this.maxIterations = maxIterations;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void train(List<DataPoint> dataPoints) {
        int numFeatures = weights[0].length - 1;
        int numClasses = weights.length;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            for (DataPoint dataPoint : dataPoints) {
                double[] features = new double[numFeatures + 1];
                features[0] = 1.0; // Bias term

                // Copy the data point features to the features array
                features[1] = dataPoint.getX();
                features[2] = dataPoint.getY();

                int trueClass = dataPoint.getTrueClass();

                double[] classProbabilities = softmax(features);

                for (int i = 0; i < numClasses; i++) {
                    double error = (i == trueClass ? 1.0 : 0.0) - classProbabilities[i];

                    for (int j = 0; j < numFeatures + 1; j++) {
                        weights[i][j] += learningRate * error * features[j];
                    }
                }
            }
        }
    }

    public int predictClass(double[] features) {
        int numClasses = weights.length;
        double[] classProbabilities = softmax(features);
        double maxProbability = -1.0;
        int predictedClass = -1;

        for (int i = 0; i < numClasses; i++) {
            if (classProbabilities[i] > maxProbability) {
                maxProbability = classProbabilities[i];
                predictedClass = i;
            }
        }

        return predictedClass;
    }

    private double[] softmax(double[] input) {
        int numClasses = weights.length;
        double[] probabilities = new double[numClasses];
        double sum = 0.0;

        for (int i = 0; i < numClasses; i++) {
            double dotProduct = 0.0;

            for (int j = 0; j < input.length; j++) {
                dotProduct += weights[i][j] * input[j];
            }

            probabilities[i] = Math.exp(dotProduct);
            sum += probabilities[i];
        }

        for (int i = 0; i < numClasses; i++) {
            probabilities[i] /= sum;
        }

        return probabilities;
    }

    public double[][] getWeights() {
        return weights;
    }
}

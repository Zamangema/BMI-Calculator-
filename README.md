# BMI Calculator (Java Swing)

A simple and user-friendly **BMI (Body Mass Index) Calculator** built using Java Swing. This desktop application allows users to input their weight and height, choose between metric or imperial units, and instantly receive their BMI along with a health category.

---

## 🚀 Features

* Clean and responsive **GUI using Java Swing**
* Supports both:

  * Metric units (kg, meters)
  * Imperial units (lbs, inches)
* Calculates BMI instantly
* Displays BMI category:

  * Underweight
  * Normal weight
  * Overweight
  * Obese
  * Severely obese
* Color-coded results for easy interpretation
* Input validation with error handling

---

## 🖥️ Screenshot (Concept)

```
----------------------------------
| Weight:  [__________]          |
| Height:  [__________]          |
| ( ) Metric   ( ) Imperial      |
|                                |
|      [ Calculate BMI ]         |
|                                |
| BMI: 22.50 (Normal weight)     |
----------------------------------
```

---

## 📦 Requirements

* Java JDK 8 or higher

---

## ▶️ How to Run

1. Save the file as:

   ```
   BmiCalculator.java
   ```

2. Compile the program:

   ```
   javac BmiCalculator.java
   ```

3. Run the application:

   ```
   java BmiCalculator
   ```

---

## 🧮 How It Works

### BMI Formula

* **Metric:**

  ```
  BMI = weight / (height * height)
  ```

* **Imperial:**

  ```
  BMI = (703 * weight) / (height * height)
  ```

---

## 🏷️ BMI Categories

| BMI Range   | Category       |
| ----------- | -------------- |
| < 18.5      | Underweight    |
| 18.5 – 24.9 | Normal weight  |
| 25 – 29.9   | Overweight     |
| 30 – 34.9   | Obese          |
| ≥ 35        | Severely obese |

---

## 🎨 UI Highlights

* Uses system look and feel for native appearance
* Soft background colors for better readability
* GridBagLayout for flexible and clean layout
* Centered result display with dynamic color feedback:

  * 🔵 Underweight
  * 🟢 Normal
  * 🟠 Overweight
  * 🔴 Obese

---

## ⚠️ Error Handling

* Alerts user if:

  * Inputs are invalid (non-numeric)
  * No unit system is selected

---

## 📁 Project Structure

```
BmiCalculator.java
README.md
```

---

## 🔧 Future Improvements

* Add input placeholders (e.g., "Enter weight")
* Support for height in feet/inches format
* Save history of BMI calculations
* Add charts or visual indicators
* Improve accessibility and keyboard navigation

---

## 📜 License

This project is open-source and free to use for educational purposes.

---

## 👨‍💻 Author [Bonginkosi Zamangema Nene]

Developed as a simple Java GUI project to demonstrate:

* Event handling
* Layout management
* Basic health calculation logic

---

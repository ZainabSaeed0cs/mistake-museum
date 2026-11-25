
package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.model.PracticeQuestion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PracticeService {

    public List<PracticeQuestion> generate(Label topLabel) {

        return switch (topLabel) {

            case MISREAD_QUESTION -> List.of(

                new PracticeQuestion(
                    "Convert 3 km to metres.",
                    "3000 m",
                    topLabel
                ),

                new PracticeQuestion(
                    "Convert 750 cm to metres.",
                    "7.5 m",
                    topLabel
                ),

                new PracticeQuestion(
                    "A car travels 150 km in 3 hours. Find its average speed.",
                    "50 km/h",
                    topLabel
                ),

                new PracticeQuestion(
                    "Convert 2.5 hours into minutes.",
                    "150 minutes",
                    topLabel
                ),

                new PracticeQuestion(
                    "Find the perimeter of a rectangle with length 8 cm and width 5 cm.",
                    "26 cm",
                    topLabel
                )
            );

            case CARELESS_SLIP -> List.of(

                new PracticeQuestion(
                    "Calculate 48 + 37.",
                    "85",
                    topLabel
                ),

                new PracticeQuestion(
                    "Calculate 15 x 6.",
                    "90",
                    topLabel
                ),

                new PracticeQuestion(
                    "A body has mass 4 kg and acceleration 3 m/s^2. Calculate the force.",
                    "12 N",
                    topLabel
                ),

                new PracticeQuestion(
                    "Calculate 144 / 12.",
                    "12",
                    topLabel
                ),

                new PracticeQuestion(
                    "A resistor has a voltage of 24 V and resistance of 6 ohms. Find the current.",
                    "4 A",
                    topLabel
                )
            );

            case CONCEPT_GAP -> List.of(

                new PracticeQuestion(
                    "What is the derivative of x^3 with respect to x?",
                    "3x^2",
                    topLabel
                ),

                new PracticeQuestion(
                    "What is the SI unit of energy?",
                    "Joule (J)",
                    topLabel
                ),

                new PracticeQuestion(
                    "What is the value of cos(0 degrees)?",
                    "1",
                    topLabel
                ),

                new PracticeQuestion(
                    "What happens to the acceleration of an object moving with constant velocity in a straight line?",
                    "Its acceleration is zero (0 m/s^2).",
                    topLabel
                ),

                new PracticeQuestion(
                    "What is the slope of a vertical straight line?",
                    "Undefined",
                    topLabel
                )
            );

            case FORMULA_MISUSE -> List.of(

                new PracticeQuestion(
                    "Using A = l x w, calculate the area of a rectangle with length 9 cm and width 4 cm.",
                    "36 cm^2",
                    topLabel
                ),

                new PracticeQuestion(
                    "Using F = ma, calculate the force when mass is 5 kg and acceleration is 2 m/s^2.",
                    "10 N",
                    topLabel
                ),

                new PracticeQuestion(
                    "Using v = u + at, find final velocity when u = 4 m/s, a = 3 m/s^2 and t = 2 s.",
                    "10 m/s",
                    topLabel
                ),

                new PracticeQuestion(
                    "Using KE = 1/2 mv^2, calculate kinetic energy for a 4 kg object moving at 2 m/s.",
                    "8 J",
                    topLabel
                ),

                new PracticeQuestion(
                    "Using P = W/t, calculate power when 120 J of work is done in 6 seconds.",
                    "20 W",
                    topLabel
                )
            );

            case INCOMPLETE_ANSWER -> List.of(

                new PracticeQuestion(
                    "Solve 3x + 5 = 20. Give the value of x.",
                    "x = 5",
                    topLabel
                ),

                new PracticeQuestion(
                    "A car travels 180 metres in 30 seconds. Find its speed, including the unit.",
                    "6 m/s",
                    topLabel
                ),

                new PracticeQuestion(
                    "Find the circumference of a circle with radius 7 cm. Use pi = 22/7.",
                    "44 cm",
                    topLabel
                ),

                new PracticeQuestion(
                    "A force of 30 N acts on a body of mass 6 kg. Find its acceleration, including the unit.",
                    "5 m/s^2",
                    topLabel
                ),

                new PracticeQuestion(
                    "Solve x^2 = 64 for the positive value of x.",
                    "x = 8",
                    topLabel
                )
            );
        };
    }
}

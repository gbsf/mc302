package pt.c02classes.s01knowledge.s02app.app;

import pt.c02classes.s01knowledge.s01base.impl.BaseConhecimento;
import pt.c02classes.s01knowledge.s01base.impl.Statistics;
import pt.c02classes.s01knowledge.s01base.inter.IBaseConhecimento;
import pt.c02classes.s01knowledge.s01base.inter.IEnquirer;
import pt.c02classes.s01knowledge.s01base.inter.IResponder;
import pt.c02classes.s01knowledge.s01base.inter.IStatistics;
import pt.c02classes.s01knowledge.s02app.actors.EnquirerAnimals;
import pt.c02classes.s01knowledge.s02app.actors.EnquirerMaze;
import pt.c02classes.s01knowledge.s02app.actors.ResponderAnimals;
import pt.c02classes.s01knowledge.s02app.actors.ResponderMaze;

import java.util.Scanner;

public class OrchestratorInit {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Escolha o tipo de desafio (Animals/Maze): ");
        String tipo = in.nextLine();

        if ("animals".equalsIgnoreCase(tipo)) {

            System.out.print("Digite o animal desejado: ");
            String animal = in.nextLine().toLowerCase();

            IBaseConhecimento base = new BaseConhecimento();
            base.setScenario("animals");

            System.out.printf("Enquirer com %s...%n", animal);
            IStatistics stat = new Statistics();
            IResponder resp = new ResponderAnimals(stat, animal);
            IEnquirer enq = new EnquirerAnimals();
            enq.connect(resp);
            enq.discover();
            System.out.println("--------------------------------------------------------------------------------\n");
        }
        else if ("maze".equalsIgnoreCase(tipo)) {

            System.out.print("Digite o labirinto desejado: ");
            String maze = in.nextLine().toLowerCase();


            System.out.printf("Enquirer com %s...%n", maze);
            IStatistics stat = new Statistics();
            IResponder resp = new ResponderMaze(stat, maze);
            IEnquirer enq = new EnquirerMaze();
            enq.connect(resp);
            enq.discover();
            System.out.println("--------------------------------------------------------------------------------\n");
        }
        else
            System.out.println("Tipo não reconhecido");

        in.close();
    }
}

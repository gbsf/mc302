package pt.c02classes.s01knowledge.s02app.actors;

import pt.c02classes.s01knowledge.s01base.inter.IEnquirer;
import pt.c02classes.s01knowledge.s01base.inter.IResponder;
import pt.c02classes.s01knowledge.s02app.maze.PointOfInterest;
import pt.c02classes.s01knowledge.s02app.util.Direction;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;

public class EnquirerMaze implements IEnquirer {

    IResponder responder;

    public void connect(IResponder responder) {
        this.responder = responder;
    }

    public boolean discover() {
        Deque<PointOfInterest> stack = new ArrayDeque<>();

        PointOfInterest poi = new PointOfInterest(Direction.SOUTH, 0);
        poi.setAvailable(avaliableDirections());
        stack.push(poi);

        loop:
        while (!"saida".equals(responder.ask("aqui"))) {

            poi = stack.peek();

            EnumSet<Direction> directions = poi.getAvailable();
            directions.remove(poi.getFrom());

            while (directions.size() == 1 && directions.contains(poi.getFromRev())) {
                poi.incrDistance();
                responder.move(poi.getFromRev().getDir());

                if ("saida".equals(responder.ask("aqui")))
                    break loop;


                avaliableDirections(directions);
                directions.remove(poi.getFrom());
            }

            // Beco sem saída
            if (directions.isEmpty()) {
                // O stack não pode estar vazio
                assert !stack.isEmpty();
                stack.pop();

                Direction back = poi.getFrom();
                // Voltar até o último ponto de interesse
                for (int i = 0, steps = poi.getDistance(); i < steps; i++)
                    responder.move(back.getDir());

                continue;
            }

            // Estamos em uma encruzilhada, virar sempre à direita
            Direction dir = poi.getFrom().previous();
            while (!directions.contains(dir))
                dir = dir.previous();
            directions.remove(dir);
            poi.setAvailable(directions);

            responder.move(dir.getDir());
            poi = new PointOfInterest(dir.reverse(), 1);
            poi.setAvailable(avaliableDirections());
            stack.push(poi);

        }

        boolean resposta = responder.finalAnswer("aqui");

        if (resposta)
            System.out.println("Você encontrou a saida!");
        else
            System.out.println("Fuém fuém fuém!");

        return resposta;
    }

    private EnumSet<Direction> avaliableDirections() {
        return avaliableDirections(EnumSet.noneOf(Direction.class));
    }

    private EnumSet<Direction> avaliableDirections(EnumSet<Direction> directions) {
        directions.clear();

        for (Direction dir : Direction.values()) {
            String answer = responder.ask(dir.getDir());
            if (!("parede".equals(answer) || "mundo".equals(answer)))
                directions.add(dir);
        }

        return directions;
    }

}

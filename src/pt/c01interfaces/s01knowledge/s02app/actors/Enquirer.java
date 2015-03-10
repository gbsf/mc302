package pt.c01interfaces.s01knowledge.s02app.actors;

import pt.c01interfaces.s01knowledge.s01base.impl.BaseConhecimento;
import pt.c01interfaces.s01knowledge.s01base.inter.IBaseConhecimento;
import pt.c01interfaces.s01knowledge.s01base.inter.IDeclaracao;
import pt.c01interfaces.s01knowledge.s01base.inter.IEnquirer;
import pt.c01interfaces.s01knowledge.s01base.inter.IObjetoConhecimento;
import pt.c01interfaces.s01knowledge.s01base.inter.IResponder;

import java.util.*;

public class Enquirer implements IEnquirer {

    List<String> respostasPossiveis = new ArrayList<String>();
    Map<String, PerguntaPorAnimal> perguntas = new HashMap<String, PerguntaPorAnimal>();

    public Enquirer() {
        IBaseConhecimento bc = new BaseConhecimento();

        String[] nomes = bc.listaNomes();

        // Analizar todas as perguntas de todos os animais
        for (String nome : nomes) {
            respostasPossiveis.add(nome);
            IObjetoConhecimento obj = bc.recuperaObjeto(nome);

            for (IDeclaracao decl = obj.primeira(); decl != null; decl = obj.proxima()) {
                String perguntaString = decl.getPropriedade();
                PerguntaPorAnimal pergunta = perguntas.get(perguntaString);

                // Criar nova pergunta se não existir
                if (pergunta == null)
                    pergunta = new PerguntaPorAnimal(perguntaString);

                if ("sim".equals(decl.getValor()))
                    pergunta.getSims().add(nome);
                else if ("nao".equals(decl.getValor()))
                    pergunta.getNaos().add(nome);
                perguntas.put(perguntaString, pergunta);
            }
        }
    }


    @Override
    public void connect(IResponder responder) {

        List<PerguntaPorAnimal> perguntasSorted = new ArrayList<PerguntaPorAnimal>(perguntas.values());
        // Organizar as perguntas pela ordem definida
        Collections.sort(perguntasSorted);

        for (PerguntaPorAnimal pergunta : perguntasSorted) {
            // Fazer a pergunta
            String resposta = responder.ask(pergunta.getPergunta());

            // Se a resposta foi "sim", manter como candidatos somente aqueles que têm "sim" como resposta
            if ("sim".equals(resposta))
                respostasPossiveis.retainAll(pergunta.getSims());
            // Idem para "nao"
            else if ("nao".equals(resposta))
                respostasPossiveis.retainAll(pergunta.getNaos());

            // Se sobrou somente uma opção, parar de fazer perguntas
            if (respostasPossiveis.size() <= 1)
                break;
        }

        // É impossível não ter sobrado nenhuma resposta.
        assert !respostasPossiveis.isEmpty();

        // Se, depois de todas as perguntas, ainda tiver mais de um possível, chutar o primeiro.
        // Senão, a lista só terá um elemento de qualquer maneira.
        boolean acertei = responder.finalAnswer(respostasPossiveis.get(0));

        if (acertei)
            System.out.println("Oba! Acertei!");
        else
            System.out.println("fuem! fuem! fuem!");
    }


    /**
     * Classe que guarda uma pergunta, bem como quais animais responderam sim e quais responderam não.
     */
    private static class PerguntaPorAnimal implements Comparable<PerguntaPorAnimal> {

        private final String pergunta;
        private final Collection<String> sims;
        private final Collection<String> naos;

        public PerguntaPorAnimal(String pergunta) {
            this.pergunta = pergunta;
            sims = new ArrayList<String>();
            naos = new ArrayList<String>();
        }

        public String getPergunta() {
            return pergunta;
        }

        public Collection<String> getSims() {
            return sims;
        }

        public Collection<String> getNaos() {
            return naos;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof PerguntaPorAnimal))
                return false;
            PerguntaPorAnimal outro = (PerguntaPorAnimal) obj;

            return this.pergunta.equals(outro.pergunta);
        }

        /**
         * Compara qual objeto tem a menor lista de animais que responderam à essa pergunta.
         */
        @Override
        public int compareTo(PerguntaPorAnimal o) {
            int tam = Math.min(sims.size(), naos.size());
            int tamOutro = Math.min(o.sims.size(), o.naos.size());

            if (tamOutro < tam)
                return -1;
            else if (tamOutro > tam)
                return  1;
            else
                return  0;
        }
    }

}

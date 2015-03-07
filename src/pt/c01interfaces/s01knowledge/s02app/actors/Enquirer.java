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
    Map<String, Par<Collection<String>, Collection<String>>> perguntas =
            new HashMap<String, Par<Collection<String>, Collection<String>>>();

    public Enquirer() {
        IBaseConhecimento bc = new BaseConhecimento();

        String[] nomes = bc.listaNomes();

        for (String nome : nomes) {
            respostasPossiveis.add(nome);
            IObjetoConhecimento obj = bc.recuperaObjeto(nome);

            for (IDeclaracao decl = obj.primeira(); decl != null; decl = obj.proxima()) {
                Par<Collection<String>, Collection<String>> candidatos = perguntas.get(decl.getPropriedade());
                if (candidatos == null)
                    candidatos = new Par<Collection<String>, Collection<String>>(
                        new ArrayList<String>(), new ArrayList<String>());

                if ("sim".equals(decl.getValor()))
                    candidatos.getPrimeiro().add(nome);
                else if ("nao".equals(decl.getValor()))
                    candidatos.getSegundo().add(nome);
                perguntas.put(decl.getPropriedade(), candidatos);
            }
        }
    }


    @Override
    public void connect(IResponder responder) {

        for (Map.Entry<String, Par<Collection<String>, Collection<String>>> entry : perguntas.entrySet()) {
            // Fazer a pergunta
            String resposta = responder.ask(entry.getKey());

            // Se a resposta foi "sim", manter como candidatos somente aqueles que têm "sim" como resposta
            if ("sim".equals(resposta))
                respostasPossiveis.retainAll(entry.getValue().getPrimeiro());
            // Idem para "nao"
            else if ("nao".equals(resposta))
                respostasPossiveis.retainAll(entry.getValue().getSegundo());

            // Se sobrou somente uma opção, parar de fazer perguntas
            if (respostasPossiveis.size() == 1)
                break;
        }

        // Se, depois de todas as perguntas, ainda tiver mais de um possível, chutar o primeiro
        boolean acertei = responder.finalAnswer(respostasPossiveis.get(0));

        if (acertei)
            System.out.println("Oba! Acertei!");
        else
            System.out.println("fuem! fuem! fuem!");
    }

    // Objeto para guardar um par de elementos imutáveis
    private static class Par<T1, T2> {
        private final T1 primeiro;
        private final T2 segundo;

        public Par(T1 primeiro, T2 segundo) {
            this.primeiro = primeiro;
            this.segundo = segundo;
        }

        public T1 getPrimeiro() {
            return primeiro;
        }

        public T2 getSegundo() {
            return segundo;
        }
    }

}

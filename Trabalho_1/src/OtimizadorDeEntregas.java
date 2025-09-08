import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class OtimizadorDeEntregas {
    private long minTempoGlobal = Long.MAX_VALUE;
    private List<Integer> melhorDivisaoMoto1 = new ArrayList();
    private List<Integer> melhorDivisaoMoto2 = new ArrayList();

    public static void main(String[] args) {
        String nomeDoArquivo = "Trabalho_1/entregas.txt";
        List<Integer> temposDeEntrega = lerTemposDeArquivo(nomeDoArquivo);
        if (temposDeEntrega.isEmpty()) {
            System.out.println("Nenhum dado válido foi encontrado no arquivo ou o arquivo não existe. Encerrando.");
        } else {
            System.out.println("Tempos lidos do arquivo '" + nomeDoArquivo + "': " + String.valueOf(temposDeEntrega));
            System.out.println("Numero de entregas = " + temposDeEntrega.size());
            System.out.println("--------------------------------------------------");
            OtimizadorDeEntregas otimizador = new OtimizadorDeEntregas();
            otimizador.encontrarSolucaoOtima(temposDeEntrega);
        }
    }

    public static List<Integer> lerTemposDeArquivo(String nomeArquivo) {
        List<Integer> tempos = new ArrayList();

        try (Scanner leitor = new Scanner(new File(nomeArquivo))) {
            while(leitor.hasNextInt()) {
                tempos.add(leitor.nextInt());
            }
        } catch (FileNotFoundException erro) {
            System.err.println("ERRO: O arquivo '" + nomeArquivo + "' não foi encontrado!");
        }

        return tempos;
    }

    public void encontrarSolucaoOtima(List<Integer> temposDeEntrega) {
        this.buscarMelhorDivisao(0, temposDeEntrega, new ArrayList(), new ArrayList());
        System.out.println("O menor tempo possível para todas as entregas é: " + this.minTempoGlobal + " minutos.");
        Collections.sort(this.melhorDivisaoMoto1);
        Collections.sort(this.melhorDivisaoMoto2);
        System.out.println("Entregas do Motoqueiro 1: " + String.valueOf(this.melhorDivisaoMoto1));
        System.out.println("Entregas do Motoqueiro 2: " + String.valueOf(this.melhorDivisaoMoto2));
    }

    private void buscarMelhorDivisao(int index, List<Integer> todasEntregas, List<Integer> entregasMoto1, List<Integer> entregasMoto2) {
        if (index == todasEntregas.size()) {
            long tempoMoto1 = this.calcularTempoFinalDeUmMotoqueiro(entregasMoto1);
            long tempoMoto2 = this.calcularTempoFinalDeUmMotoqueiro(entregasMoto2);
            long tempoMaximoDaDivisao = Math.max(tempoMoto1, tempoMoto2);
            if (tempoMaximoDaDivisao < this.minTempoGlobal) {
                this.minTempoGlobal = tempoMaximoDaDivisao;
                this.melhorDivisaoMoto1 = new ArrayList(entregasMoto1);
                this.melhorDivisaoMoto2 = new ArrayList(entregasMoto2);
            }

        } else {
            Integer entregaAtual = (Integer)todasEntregas.get(index);
            entregasMoto1.add(entregaAtual);
            this.buscarMelhorDivisao(index + 1, todasEntregas, entregasMoto1, entregasMoto2);
            entregasMoto1.remove(entregasMoto1.size() - 1);
            entregasMoto2.add(entregaAtual);
            this.buscarMelhorDivisao(index + 1, todasEntregas, entregasMoto1, entregasMoto2);
            entregasMoto2.remove(entregasMoto2.size() - 1);
        }
    }

    private long calcularTempoFinalDeUmMotoqueiro(List<Integer> entregas) {
        if (entregas.isEmpty()) {
            return 0L;
        } else {
            List<Integer> entregasOrdenadas = new ArrayList(entregas);
            Collections.sort(entregasOrdenadas);
            long tempoLivre = 0L;
            long tempoConclusaoFinal = 0L;

            for(int tempoIda : entregasOrdenadas) {
                tempoConclusaoFinal = tempoLivre + (long)tempoIda;
                tempoLivre += (long)tempoIda * 2L;
            }

            return tempoConclusaoFinal;
        }
    }
}

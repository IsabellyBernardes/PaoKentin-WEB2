// Função para formatar o tempo (adiciona um zero à esquerda se for menor que 10)
function formatarTempo(tempo) {
    return tempo < 10 ? '0' + tempo : tempo;
}

// Função principal que busca os dados da API e atualiza a tela
async function buscarStatusFornadas() {
    const container = document.getElementById('fornadas-container');

    try {
        const response = await fetch('/api/cliente/status-fornadas');
        if (!response.ok) {
            throw new Error('Erro ao buscar dados da API');
        }
        const fornadas = await response.json();

        // Limpa o container antes de adicionar os novos cards
        container.innerHTML = '';

        if (fornadas.length === 0) {
            container.innerHTML = '<p class="text-center">Nenhuma fornada registrada ainda. Volte mais tarde!</p>';
            return;
        }

        fornadas.forEach(fornada => {
            const card = document.createElement('div');
            card.className = 'col-md-4 mb-4';

            let statusHtml = '';
            if (fornada.status === 'Assando') {
                let segundosRestantes = fornada.tempoRestanteSegundos;

                // Calcula minutos e segundos para o contador
                let minutos = Math.floor(segundosRestantes / 60);
                let segundos = segundosRestantes % 60;

                statusHtml = `
                    <p class="card-text status-assando">
                        Status: <strong>${fornada.status}</strong>
                    </p>
                    <p class="card-text countdown" data-segundos-restantes="${segundosRestantes}">
                        Tempo Restante: ${formatarTempo(minutos)}:${formatarTempo(segundos)}
                    </p>`;
            } else {
                statusHtml = `
                    <p class="card-text status-pronto">
                        Status: <strong>${fornada.status}! Pode vir buscar!</strong>
                    </p>`;
            }

            card.innerHTML = `
                <div class="card shadow-sm">
                    <div class="card-header bg-light">
                        ${fornada.nomePao}
                    </div>
                    <div class="card-body">
                        <p class="card-text">${fornada.descricaoPao}</p>
                        ${statusHtml}
                    </div>
                </div>
            `;
            container.appendChild(card);
        });

    } catch (error) {
        container.innerHTML = '<p class="text-center text-danger">Não foi possível carregar as informações das fornadas. Tente novamente mais tarde.</p>';
        console.error('Erro:', error);
    }
}

let intervalId = null; // Variável para controlar o intervalo do contador

// Função para atualizar os contadores a cada segundo
function iniciarContadores() {
    // Limpa qualquer intervalo anterior para evitar múltiplos contadores rodando
    if (intervalId) {
        clearInterval(intervalId);
    }

    intervalId = setInterval(() => {
        const contadores = document.querySelectorAll('.countdown');
        if (contadores.length === 0) {
            clearInterval(intervalId); // Para o intervalo se não houver mais contadores
            return;
        }

        contadores.forEach(elem => {
            let totalSegundos = parseInt(elem.dataset.segundosRestantes, 10);

            if (totalSegundos > 0) {
                totalSegundos--;
                elem.dataset.segundosRestantes = totalSegundos; // Atualiza o atributo data-*

                const minutos = Math.floor(totalSegundos / 60);
                const segundos = totalSegundos % 60;
                elem.textContent = `Tempo Restante: ${formatarTempo(minutos)}:${formatarTempo(segundos)}`;
            } else {
                // Quando o tempo acabar, busca os dados novamente para atualizar o status para "Pronto"
                buscarStatusFornadas().then(iniciarContadores);
            }
        });
    }, 1000);
}


// Função de inicialização
async function initCliente() {
    await buscarStatusFornadas();
    iniciarContadores();
    setInterval(async () => {
        await buscarStatusFornadas();
        iniciarContadores();
    }, 30000); // Atualiza os dados da API a cada 30 segundos
}

// Chama a função de inicialização quando a página carregar
document.addEventListener('DOMContentLoaded', initCliente);
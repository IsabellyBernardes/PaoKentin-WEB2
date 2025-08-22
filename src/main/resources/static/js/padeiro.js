document.addEventListener('DOMContentLoaded', () => {
    // --- Referências aos Elementos do DOM ---
    const container = document.getElementById('botoes-pao-container');
    const btnVerStatus = document.getElementById('btn-ver-status');

    // Modal de Feedback (para registro de fornada)
    const feedbackModalElement = document.getElementById('feedbackModal');
    const feedbackModalHeader = document.getElementById('feedbackModalHeader');
    const feedbackModalBody = document.getElementById('feedbackModalBody');
    const feedbackModal = new bootstrap.Modal(feedbackModalElement);

    // Modal de Status (para ver fornadas ativas)
    const statusModalElement = document.getElementById('statusModal');
    const statusModalBody = document.getElementById('statusModalBody');
    const statusModal = new bootstrap.Modal(statusModalElement);

    // Variável de controle para o contador de tempo real do modal de status
    let statusIntervalId = null;

    // --- Funções Auxiliares ---

    /**
     * Calcula se a cor do texto sobre um fundo hexadecimal deve ser preta ou branca.
     * @param {string} hexcolor - A cor de fundo em formato hexadecimal (ex: '#FF5733').
     * @returns {'black'|'white'}
     */
    function getContrastYIQ(hexcolor){
        hexcolor = hexcolor.replace("#", "");
        const r = parseInt(hexcolor.substr(0,2),16);
        const g = parseInt(hexcolor.substr(2,2),16);
        const b = parseInt(hexcolor.substr(4,2),16);
        const yiq = ((r*299)+(g*587)+(b*114))/1000;
        return (yiq >= 128) ? 'black' : 'white';
    }

    /**
     * Formata um total de segundos para o formato MM:SS.
     * @param {number} totalSegundos - O total de segundos a ser formatado.
     * @returns {string} O tempo formatado.
     */
    function formatarTempoRestante(totalSegundos) {
        if (totalSegundos <= 0) return '00:00';
        const minutos = Math.floor(totalSegundos / 60);
        const segundos = totalSegundos % 60;
        const pad = (num) => num < 10 ? '0' + num : num;
        return `${pad(minutos)}:${pad(segundos)}`;
    }

    // --- Funções Principais ---

    /**
     * Exibe o modal de feedback com uma mensagem de sucesso ou erro.
     * @param {string} mensagem - A mensagem a ser exibida.
     * @param {boolean} sucesso - True para sucesso (verde), false para erro (vermelho).
     */
    function mostrarFeedback(mensagem, sucesso = true) {
        feedbackModalBody.textContent = mensagem;
        if (sucesso) {
            feedbackModalHeader.classList.remove('bg-danger');
            feedbackModalHeader.classList.add('bg-success');
        } else {
            feedbackModalHeader.classList.remove('bg-success');
            feedbackModalHeader.classList.add('bg-danger');
        }
        feedbackModal.show();
    }

    /**
     * Busca os pães cadastrados na API e cria os botões na tela.
     */
    async function carregarBotoesDePao() {
        try {
            const response = await fetch('/api/paes');
            if (!response.ok) throw new Error('Erro ao buscar lista de pães.');
            const paes = await response.json();

            container.innerHTML = '';

            paes.forEach(pao => {
                const col = document.createElement('div');
                col.className = 'col-md-4';

                const button = document.createElement('button');
                button.className = 'btn btn-lg w-100 btn-pao';
                button.textContent = pao.nome;

                // Aplica a cor personalizada vinda da API
                if (pao.corHex) {
                    button.style.backgroundColor = pao.corHex;
                    button.style.borderColor = pao.corHex;
                    button.style.color = getContrastYIQ(pao.corHex);
                }

                button.onclick = () => {
                    registrarFornada(pao.id, pao.nome);
                };

                col.appendChild(button);
                container.appendChild(col);
            });

        } catch (error) {
            container.innerHTML = '<p class="text-center text-danger">Não foi possível carregar os tipos de pão.</p>';
            console.error('Erro:', error);
        }
    }

    /**
     * Envia uma requisição para registrar uma nova fornada.
     * @param {number} paoId - O ID do pão da fornada.
     * @param {string} nomePao - O nome do pão, para usar na mensagem de feedback.
     */
    async function registrarFornada(paoId, nomePao) {
        try {
            const response = await fetch(`/api/padeiro/fornada/${paoId}`, { method: 'POST' });
            if (response.status === 201) {
                mostrarFeedback(`Fornada de ${nomePao} registrada com sucesso!`);
            } else {
                throw new Error(`Falha ao registrar fornada. Status: ${response.status}`);
            }
        } catch (error) {
            mostrarFeedback('Erro de comunicação com o servidor. Tente novamente.', false);
            console.error('Erro ao registrar fornada:', error);
        }
    }

    /**
     * Busca o status das fornadas ativas e as exibe no modal de status.
     */
    async function visualizarFornadasAtivas() {
        try {
            const response = await fetch('/api/cliente/status-fornadas');
            if(!response.ok) throw new Error('Falha ao buscar status');
            const todasFornadas = await response.json();

            const fornadasAtivas = todasFornadas.filter(f => f.status === 'Assando');

            if (fornadasAtivas.length === 0) {
                statusModalBody.innerHTML = '<p class="text-center">Nenhuma fornada assando no momento.</p>';
            } else {
                let listaHtml = '<ul class="list-group">';
                fornadasAtivas.forEach(fornada => {
                    listaHtml += `
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <strong>${fornada.nomePao}</strong>
                            <span class="badge bg-danger rounded-pill fs-6 countdown-status" data-segundos-restantes="${fornada.tempoRestanteSegundos}">
                                ${formatarTempoRestante(fornada.tempoRestanteSegundos)}
                            </span>
                        </li>
                    `;
                });
                listaHtml += '</ul>';
                statusModalBody.innerHTML = listaHtml;
            }
            statusModal.show();
        } catch(error) {
            statusModalBody.innerHTML = '<p class="text-center text-danger">Erro ao carregar o status das fornadas.</p>';
            statusModal.show();
            console.error(error);
        }
    }

    // --- Lógica do Contador em Tempo Real ---

    function iniciarContadorStatus() {
        if (statusIntervalId) clearInterval(statusIntervalId);
        statusIntervalId = setInterval(() => {
            const contadores = statusModalBody.querySelectorAll('.countdown-status');
            if (contadores.length === 0) {
                clearInterval(statusIntervalId);
                return;
            }
            contadores.forEach(elem => {
                let totalSegundos = parseInt(elem.dataset.segundosRestantes, 10);
                if (totalSegundos > 0) {
                    totalSegundos--;
                    elem.dataset.segundosRestantes = totalSegundos;
                    elem.textContent = formatarTempoRestante(totalSegundos);
                } else {
                    elem.textContent = "Pronto!";
                    elem.classList.remove('bg-danger');
                    elem.classList.add('bg-success');
                }
            });
        }, 1000);
    }

    function pararContadorStatus() {
        clearInterval(statusIntervalId);
    }

    // --- Inicialização e Event Listeners ---

    // Associa a função de visualização ao clique do botão
    btnVerStatus.addEventListener('click', visualizarFornadasAtivas);

    // Controla o início e o fim do contador baseado na visibilidade do modal de status
    statusModalElement.addEventListener('shown.bs.modal', iniciarContadorStatus);
    statusModalElement.addEventListener('hidden.bs.modal', pararContadorStatus);

    // Carrega os botões principais ao iniciar a página
    carregarBotoesDePao();
});
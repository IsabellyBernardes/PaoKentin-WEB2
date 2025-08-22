document.addEventListener('DOMContentLoaded', () => {

    const container = document.getElementById('botoes-pao-container');
    const btnVerStatus = document.getElementById('btn-ver-status');


    const feedbackModalElement = document.getElementById('feedbackModal');
    const feedbackModalHeader = document.getElementById('feedbackModalHeader');
    const feedbackModalBody = document.getElementById('feedbackModalBody');
    const feedbackModal = new bootstrap.Modal(feedbackModalElement);


    const statusModalElement = document.getElementById('statusModal');
    const statusModalBody = document.getElementById('statusModalBody');
    const statusModal = new bootstrap.Modal(statusModalElement);


    let statusIntervalId = null;

    function getContrastYIQ(hexcolor){
        hexcolor = hexcolor.replace("#", "");
        const r = parseInt(hexcolor.substr(0,2),16);
        const g = parseInt(hexcolor.substr(2,2),16);
        const b = parseInt(hexcolor.substr(4,2),16);
        const yiq = ((r*299)+(g*587)+(b*114))/1000;
        return (yiq >= 128) ? 'black' : 'white';
    }


    function formatarTempoRestante(totalSegundos) {
        if (totalSegundos <= 0) return '00:00';
        const minutos = Math.floor(totalSegundos / 60);
        const segundos = totalSegundos % 60;
        const pad = (num) => num < 10 ? '0' + num : num;
        return `${pad(minutos)}:${pad(segundos)}`;
    }

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

    btnVerStatus.addEventListener('click', visualizarFornadasAtivas);

    statusModalElement.addEventListener('shown.bs.modal', iniciarContadorStatus);
    statusModalElement.addEventListener('hidden.bs.modal', pararContadorStatus);

    carregarBotoesDePao();
});
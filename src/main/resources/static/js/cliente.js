function formatarTempo(tempo) {
    return tempo < 10 ? '0' + tempo : tempo;
}


async function buscarStatusFornadas() {
    const container = document.getElementById('fornadas-container');

    try {
        const response = await fetch('/api/cliente/status-fornadas');
        if (!response.ok) {
            throw new Error('Erro ao buscar dados da API');
        }
        const fornadas = await response.json();

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

let intervalId = null;

function iniciarContadores() {
    if (intervalId) {
        clearInterval(intervalId);
    }

    intervalId = setInterval(() => {
        const contadores = document.querySelectorAll('.countdown');
        if (contadores.length === 0) {
            clearInterval(intervalId);
            return;
        }

        contadores.forEach(elem => {
            let totalSegundos = parseInt(elem.dataset.segundosRestantes, 10);

            if (totalSegundos > 0) {
                totalSegundos--;
                elem.dataset.segundosRestantes = totalSegundos;

                const minutos = Math.floor(totalSegundos / 60);
                const segundos = totalSegundos % 60;
                elem.textContent = `Tempo Restante: ${formatarTempo(minutos)}:${formatarTempo(segundos)}`;
            } else {
                buscarStatusFornadas().then(iniciarContadores);
            }
        });
    }, 1000);
}

async function initCliente() {
    await buscarStatusFornadas();
    iniciarContadores();
    setInterval(async () => {
        await buscarStatusFornadas();
        iniciarContadores();
    }, 30000);
}

document.addEventListener('DOMContentLoaded', initCliente);
document.addEventListener('DOMContentLoaded', () => {
    const listaPaesContainer = document.getElementById('lista-paes');
    const formCadastro = document.getElementById('form-cadastro-pao');
    const formEdicao = document.getElementById('form-edicao-pao');

    const feedbackToastElement = document.getElementById('feedbackToast');
    const feedbackToast = new bootstrap.Toast(feedbackToastElement);
    const toastBody = document.getElementById('feedbackToastBody');
    const detalheModal = new bootstrap.Modal(document.getElementById('detalheModal'));
    const detalheModalTitle = document.getElementById('detalheModalTitle');
    const detalheModalBody = document.getElementById('detalheModalBody');
    const editModal = new bootstrap.Modal(document.getElementById('editModal'));

    let paesCache = [];

    function mostrarFeedback(mensagem, sucesso = true) {
        toastBody.textContent = mensagem;
        const toastHeader = feedbackToastElement.querySelector('.toast-header');
        if (sucesso) {
            toastHeader.classList.remove('bg-danger');
            toastHeader.classList.add('bg-success');
        } else {
            toastHeader.classList.remove('bg-success');
            toastHeader.classList.add('bg-danger');
        }
        feedbackToast.show();
    }

    function exibirDetalhes(pao) {
        detalheModalTitle.textContent = pao.nome;
        detalheModalBody.innerHTML = `
            <p><strong>ID:</strong> ${pao.id}</p>
            <p><strong>Descrição:</strong> ${pao.descricao}</p>
            <p><strong>Tempo de Preparo:</strong> ${pao.tempoPreparoMinutos} minutos</p>
            <p><strong>Cor do Botão:</strong> 
                <span class="d-inline-block p-2 border" style="background-color: ${pao.corHex};"></span>
                ${pao.corHex}
            </p>
        `;
        detalheModal.show();
    }

    function abrirModalEdicao(pao) {
        document.getElementById('edit-pao-id').value = pao.id;
        document.getElementById('edit-nome').value = pao.nome;
        document.getElementById('edit-tempoPreparo').value = pao.tempoPreparoMinutos;
        editModal.show();
    }

    async function carregarPaes() {
        try {
            const response = await fetch('/api/paes');
            if (!response.ok) throw new Error('Falha ao carregar pães.');
            paesCache = await response.json();

            listaPaesContainer.innerHTML = '';
            if (paesCache.length === 0) {
                listaPaesContainer.innerHTML = '<li class="list-group-item">Nenhum pão cadastrado.</li>';
                return;
            }

            paesCache.forEach(pao => {
                const li = document.createElement('li');
                li.className = 'list-group-item d-flex justify-content-between align-items-center';
                li.innerHTML = `
                    <div>
                        <span class="d-inline-block p-2 me-2 border" style="background-color: ${pao.corHex};"></span>
                        <strong class="align-middle">${pao.nome}</strong>
                    </div>
                    <div>
                        <span class="badge bg-primary rounded-pill me-2">${pao.tempoPreparoMinutos} min</span>
                        <button class="btn btn-sm btn-outline-secondary btn-detalhes" title="Ver Detalhes"><i class="bi bi-eye"></i></button>
                        <button class="btn btn-sm btn-outline-primary btn-editar" title="Editar Tempo"><i class="bi bi-pencil"></i></button>
                    </div>
                `;

                li.querySelector('.btn-detalhes').addEventListener('click', () => exibirDetalhes(pao));
                li.querySelector('.btn-editar').addEventListener('click', () => abrirModalEdicao(pao));

                listaPaesContainer.appendChild(li);
            });
        } catch (error) {
            listaPaesContainer.innerHTML = '<li class="list-group-item text-danger">Erro ao carregar lista.</li>';
            console.error(error);
        }
    }

    async function cadastrarPao(event) {
        event.preventDefault();
        const nome = document.getElementById('nome').value;
        const descricao = document.getElementById('descricao').value;
        const tempoPreparo = document.getElementById('tempoPreparo').value;
        const corHex = document.getElementById('corHex').value;
        const novoPao = { nome, descricao, tempoPreparoMinutos: parseInt(tempoPreparo, 10), corHex };

        try {
            const response = await fetch('/api/paes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(novoPao)
            });

            if (response.status === 201) {
                mostrarFeedback('Pão cadastrado com sucesso!');
                formCadastro.reset();
                await carregarPaes();
            } else {
                throw new Error('Falha no cadastro.');
            }
        } catch (error) {
            mostrarFeedback('Erro ao cadastrar pão. Tente novamente.', false);
            console.error(error);
        }
    }

    async function salvarAlteracoes(event) {
        event.preventDefault();
        const paoId = document.getElementById('edit-pao-id').value;
        const novoTempo = document.getElementById('edit-tempoPreparo').value;

        const paoParaAtualizar = paesCache.find(p => p.id == paoId);
        if (!paoParaAtualizar) {
            mostrarFeedback("Erro: Pão não encontrado para atualização.", false);
            return;
        }

        paoParaAtualizar.tempoPreparoMinutos = parseInt(novoTempo, 10);

        try {
            const response = await fetch(`/api/paes/${paoId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(paoParaAtualizar)
            });

            if (response.ok) {
                mostrarFeedback('Tempo de preparo atualizado com sucesso!');
                editModal.hide();
                await carregarPaes();
            } else {
                throw new Error('Falha na atualização.');
            }
        } catch (error) {
            mostrarFeedback('Erro ao salvar alterações. Tente novamente.', false);
            console.error(error);
        }
    }

    formCadastro.addEventListener('submit', cadastrarPao);
    formEdicao.addEventListener('submit', salvarAlteracoes);

    carregarPaes();
});
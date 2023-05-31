package com.example.tutorialjetpackcompose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tutorialjetpackcompose.ui.theme.TutorialJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * setContent usado para definir o layout
         * ao inves de chamar o XML, chama-se as fun de composição
         */
        setContent {
            /**
             * NomeDoSeuApp+Theme é uma maneira de definir o estilo de fun de composição
             */
            TutorialJetpackComposeTheme {
                /**
                 *
                 * Modifier é opcional.
                 * Os modificadores informam para o elemento da IU como serão dispostos, exibidos
                 * ou se comportarão no layout pai.
                 */
                MyApp(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @Composable
    fun MyApp(
        modifier: Modifier = Modifier
    ) {
        var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

        Surface(modifier) {
            if (shouldShowOnboarding) {
                OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
            } else {
                Greetings()
            }
        }
    }


    @Composable
    fun OnboardingScreen(
        onContinueClicked: () -> Unit,
        modifier: Modifier = Modifier
    ) {

        /**
         * Column config para exibir o conteudo no centro da tela.
         */
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to the Basic Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text(text = "Continue")
            }
        }

    }

    @Composable
    private fun Greetings(
        modifier: Modifier = Modifier,
        names: List<String> = List(1000) { "$it" }
    ) {
        /**
         * LazyColumn e LazyRow são equivalentes ao RECYCLERVIEW.
         * LazyColumn não recicla os filhos como o recyclerview, ele emite novas composições conforme
         * você rola e ainda apresenta uma boa performace, ja que é mais leve em relação a instanciar
         * Views.
         */
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(items = names) { name ->
                Greeting(name = name)
            }
        }
    }

    @Preview(showBackground = true, widthDp = 320, heightDp = 320)
    @Composable
    fun OnboardingPreview() {
        TutorialJetpackComposeTheme {
            OnboardingScreen(onContinueClicked = {})
        }
    }

    /**
     * fun de composição (anotada com @Composable).
     * permite com que chame outras fun de composição dentro dela.
     * Elementos basicos de layout do Compose: COLUMN, ROW, BOX.
     * Pode-se colocar itens em cada elemento do layout.
     */
    @Composable
    fun Greeting(name: String) {
        /**
         * Recomposição: Quando o compose nota alteração nos dados e executa novamente as funcoes de
         * composição para atualizar os dados, criando assim uma IU atualizada.
         *
         * O Compose também analisa os dados necessários para uma composição individual, assim ele
         * só faz a recomposição de componentes que precisam dela, deixando aqueles que nao precisam
         * passarem.
         */
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            CardContent(name)
        }
    }

    @Composable
    fun CardContent(name: String) {
        /**
         * remember PROTEGE contra a recomposição, para que o estado não seja redefinido.
         * remember funciona SOMENTE ENQUANTO A COMPOSIÇÃO FOR MANTIDA!(quando se faz a rotação de
         * tela e/ou interrompemos o processo por ex, toda atividade é reiniciada e o estado é
         * perdido.
         * rememberSaveable salva para todas as mudanças.
         */
        var expanded by rememberSaveable { mutableStateOf(false) }

        Row(modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            /**
             * Weight faz com que o elemento preencha esse espaço que esta disponivel
             * Column irá alinhar os elementos na vertical.
             */
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(text = "Hello, ")
                Text(
                    text = name, style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                /**
                 * Define o texto mostrado quando é clicado para expandir.
                 */
                if (expanded) {
                    Text(
                        text = ("Composem ipsum color sit, " +
                                "padding theme elit, send do bouncy, ").repeat(4)
                    )
                }
            }
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = if(expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if(expanded){
                        stringResource(R.string.show_less)
                    }else{
                        stringResource(R.string.show_more)
                    }
                )
            }
        }
    }

    /**
     * Para utilizar a visualização do AS, tem que marcar qualquer fun de composição com @Preview
     */
    @Preview(showBackground = true, widthDp = 320)
    /**
     * Adiconados um preview separado com o tema dark.
     */
    @Preview(
        showBackground = true,
        widthDp = 320,
        uiMode = UI_MODE_NIGHT_YES,
        name = "Dark"
    )
    @Composable
    fun DefaultPreview() {
        TutorialJetpackComposeTheme {
            Greetings()
        }
    }

    @Preview
    @Composable
    fun MyAppPreview() {
        TutorialJetpackComposeTheme {
            MyApp(Modifier.fillMaxSize())
        }
    }

}
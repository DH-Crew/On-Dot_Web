package com.example.ondot_web

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dh.ondot.presentation.ui.theme.OnDotTypo
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.launch
import ondotweb.composeapp.generated.resources.Res
import ondotweb.composeapp.generated.resources.compose_multiplatform
import ondotweb.composeapp.generated.resources.ic_capybara
import ondotweb.composeapp.generated.resources.ic_mang
import ondotweb.composeapp.generated.resources.ic_taek
import org.jetbrains.compose.resources.painterResource
import org.w3c.dom.HTMLMetaElement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    LaunchedEffect(Unit) {
        document.title = "OnDot — 팀 소개"
        ensureMeta(name = "description", content = "KMP/Compose Multiplatform로 만든 OnDot 팀 소개 페이지")
        ensureMeta(property = "og:title", content = "OnDot — 팀 소개")
        ensureMeta(property = "og:description", content = "KMP/Compose Multiplatform로 만든 OnDot 팀 소개 페이지")
        ensureMeta(property = "og:type", content = "website")
    }

    val sections = listOf(Section.Hero, Section.Team, Section.Values, Section.Contact, Section.Footer)
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    MaterialTheme(colorScheme = appColorScheme()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("OnDot", style = OnDotTypo().titleMediumL) },
                    actions = {
                        TextButton(onClick = { scrollTo(scope, listState, sections.indexOf(Section.Team)) }) { Text("팀", style = OnDotTypo().bodyLargeR1) }
                        TextButton(onClick = { scrollTo(scope, listState, sections.indexOf(Section.Values)) }) { Text("가치", style = OnDotTypo().bodyLargeR1) }
                        TextButton(onClick = { scrollTo(scope, listState, sections.indexOf(Section.Contact)) }) { Text("문의", style = OnDotTypo().bodyLargeR1) }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    HeroSection(
                        onPrimaryAction = { window.open("mailto:hello@ondot.app?subject=[문의] OnDot 팀 소개", "_self") }
                    )
                }

                item {
                    SectionTitle("팀 소개", "사용자 경험과 안정적인 아키텍처를 동시에 추구합니다.")
                    TeamSection(
                        members = sampleMembers()
                    )
                }

                item {
                    SectionTitle("우리가 믿는 가치", "빠르게 만들되, 오래 가는 구조로.")
                    ValuesSection()
                }

                item {
                    ContactSection(
                        email = "teamdh1216@gmail.com",
                        onContactClick = { window.open("mailto:teamdh1216@gmail.com?subject=[문의] OnDot 팀 소개", "_self") }
                    )
                }
            }
        }
    }
}

/* -------------------------- 단일 섹션 컴포넌트들 -------------------------- */

@Composable
private fun HeroSection(onPrimaryAction: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF111827),
                        Color(0xFF1F2937)
                    )
                )
            )
            .padding(top = 64.dp, bottom = 72.dp, start = 20.dp, end = 20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                "OnDot",
                color = Color.White,
                style = OnDotTypo().titleLargeM.copy(fontSize = 42.sp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "KMP 기반으로 실무에 바로 쓰는 제품을 만듭니다.\n" +
                        "Android · iOS · Web을 하나의 철학으로.",
                color = Color(0xFFCBD5E1),
                textAlign = TextAlign.Center,
                style = OnDotTypo().bodyLargeR1
            )
            Spacer(Modifier.height(22.dp))
            Button(onClick = onPrimaryAction) { Text("문의하기", style = OnDotTypo().bodyLargeR1) }
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp, bottom = 8.dp)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, style = OnDotTypo().bodyLargeR1, fontWeight = FontWeight.Bold)
        if (!subtitle.isNullOrBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(subtitle, style = OnDotTypo().bodyLargeR1, color = Color(0xFF4B5563))
        }
    }
}

data class TeamMember(
    val name: String,
    val role: String,
    val bio: String,
    val photo: (@Composable () -> Unit)? = null,
    val links: List<Pair<String, String>> = emptyList()
)

@Composable
private fun TeamSection(members: List<TeamMember>) {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        val cardModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

        members.chunked(3).forEach { row ->
            Column {
                row.forEach { m ->
                    MemberCard(member = m, modifier = cardModifier)
                }
            }
        }
    }
}

@Composable
private fun MemberCard(member: TeamMember, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                if (member.photo != null) {
                    member.photo.invoke()
                } else {
                    Text(member.name.take(1), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(member.name, style = OnDotTypo().bodyLargeR1)
                Text(member.role, style = OnDotTypo().bodyLargeR1, color = Color(0xFF6B7280))
                Spacer(Modifier.height(6.dp))
                Text(member.bio, color = Color(0xFF374151), style = OnDotTypo().bodyLargeR1)
            }
        }
    }
}

@Composable
private fun ValuesSection() {
    Column(Modifier.padding(horizontal = 28.dp, vertical = 12.dp)) {
        ValueCard(
            title = "Clean Architecture",
            body = "모듈러 구조와 테스트 가능한 계층으로, 기능이 커져도 유지보수가 쉬운 코드베이스를 유지합니다."
        )
        ValueCard(
            title = "Optimistic UI & 성능",
            body = "사용자가 체감하는 속도를 최우선으로. 렌더 성능/재구성 최소화를 꾸준히 측정·개선합니다."
        )
        ValueCard(
            title = "멀티플랫폼 전략",
            body = "KMP로 공통 로직을 공유하고, 플랫폼 특화 경험은 expect/actual로 풀어냅니다."
        )
    }
}

@Composable
private fun ValueCard(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = OnDotTypo().bodyMediumSB)
            Spacer(Modifier.height(6.dp))
            Text(body, color = Color(0xFF374151), style = OnDotTypo().bodyMediumSB)
        }
    }
}

@Composable
private fun ContactSection(email: String, onContactClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(28.dp)
            .background(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("함께 만들고 싶으신가요?", style = OnDotTypo().bodyLargeR1)
            Spacer(Modifier.height(8.dp))
            Text("프로덕트/협업 문의는 메일로 연락 주세요: $email", style = OnDotTypo().bodyLargeR1, textAlign = TextAlign.Center)
            Spacer(Modifier.height(14.dp))
            Button(onClick = onContactClick) { Text("이메일 보내기", style = OnDotTypo().bodyLargeR1) }
        }
    }
}

/* ------------------------------- 유틸/데이터 ------------------------------- */

private enum class Section { Hero, Team, Values, Contact, Footer }

private fun scrollTo(
    scope: kotlinx.coroutines.CoroutineScope,
    listState: androidx.compose.foundation.lazy.LazyListState,
    index: Int
) {
    scope.launch {
        listState.animateScrollToItem(index)
    }
}

private fun appColorScheme(): ColorScheme = lightColorScheme(
    primary = Color(0xFF111827),
    onPrimary = Color.White,
    surface = Color.White,
    onSurface = Color(0xFF111827)
)

private fun sampleMembers() = listOf(
    TeamMember(
        name = "손현수",
        role = "Android/KMP",
        bio = "Compose, KMP로 실무형 아키텍처를 설계/구현합니다.",
        photo = { Image(painterResource(Res.drawable.compose_multiplatform), null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop) },
    ),
    TeamMember(
        name = "박세린",
        role = "Designer",
        bio = "사용자 경험과 비주얼 아이덴티티를 책임지며, 제품의 첫인상을 만듭니다.",
        photo = { Image(painterResource(Res.drawable.ic_mang), null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop) },
    ),
    TeamMember(
        name = "오남택",
        role = "Project Manager",
        bio = "팀의 일정과 목표를 조율하며, 원활한 협업과 성공적인 결과물을 이끕니다.",
        photo = { Image(painterResource(Res.drawable.ic_taek), null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop) },
    ),
    TeamMember(
        name = "문희상",
        role = "Backend",
        bio = "안정적인 API와 배포 파이프라인을 책임집니다.",
        photo = { Image(painterResource(Res.drawable.ic_capybara), null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop) },
    )
)

/* ------------------------------ 메타 태그 보조 ------------------------------ */

private fun ensureMeta(name: String? = null, property: String? = null, content: String) {
    val head = document.head ?: return
    val selector = when {
        name != null -> "meta[name='$name']"
        property != null -> "meta[property='$property']"
        else -> return
    }
    val existing = head.querySelector(selector) as? org.w3c.dom.HTMLMetaElement
    val tag = existing ?: (document.createElement("meta") as org.w3c.dom.HTMLMetaElement).also {
        if (name != null) it.setAttribute("name", name)
        if (property != null) it.setAttribute("property", property)
        head.appendChild(it)
    }
    tag.setAttribute("content", content)
}

fun ensureMetaInBody(name: String, content: String) {
    val meta = document.createElement("meta") as HTMLMetaElement
    meta.setAttribute("name", name)
    meta.setAttribute("content", content)
    document.body?.appendChild(meta)
}

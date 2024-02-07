package ama.ripe.search.data.mapper

import ama.ripe.search.data.database.models.InetNumDbModel
import ama.ripe.search.data.database.models.OrganizationDbModel
import ama.ripe.search.data.network.model.ObjectDto
import ama.ripe.search.domain.entity.InetNumDomModel
import ama.ripe.search.domain.entity.OrganizationDomModel

fun List<ObjectDto>.toDbs(): List<OrganizationDbModel> = map { mapDtoToDbModel(it) }
fun List<ObjectDto>.toInetNumDbs(id: Int): List<InetNumDbModel> =
    map { mapDtoToInetNumDbModel(it, id) }


fun mapDtoToInetNumDbModel(dto: ObjectDto, id: Int): InetNumDbModel {
    var countryUrl = ""
    var inetNum = ""
    var netName = ""
    var checkCountry = false
    dto.mAttributes.mAttribute.forEach()
    {
        when (it.mName) {

            "inetnum" -> inetNum = it.mValue
            "netname" -> netName = it.mValue
            "country" -> {
                checkCountry = true
                countryUrl = "https://flagsapi.com/${it.mValue}/shiny/64.png"
            }

            else -> ""
        }
    }
    return InetNumDbModel(
        inetNum = inetNum,
        netName = netName,
        countryFlag = if (checkCountry) countryUrl else null,
        ownerOrgId = id
    )
}

fun mapDtoToDbModel(dto: ObjectDto): OrganizationDbModel {
    var countryUrl = ""
    var orgName = ""
    var checkCountry = false
    var organization = ""
    var address = ""
    var phone = ""
    var faxNo = ""
    dto.mAttributes.mAttribute.forEach()
    {

        when (it.mName) {
            "organisation" -> {
                organization = it.mValue
            }

            "address" -> address += it.mValue + "\n"
            "phone" -> phone = it.mValue
            "fax-no" -> faxNo = it.mValue
            "org-name" -> orgName = it.mValue
            //"org" -> orgName = it.mValue
            "country" -> {
                checkCountry = true
                countryUrl = "https://flagsapi.com/${it.mValue}/shiny/64.png"
            }

            else -> ""
        }
    }
    return OrganizationDbModel(
        orgName = orgName,
        organization = organization,
        address = address,
        phone = phone,
        faxNo = faxNo,
        countryFlag = if (checkCountry) countryUrl else null
    )
}

fun List<ObjectDto>.toEntities(): List<OrganizationDomModel> = map { mapDtoToDomModel(it) }
fun List<OrganizationDbModel>.toDomEntities(): List<OrganizationDomModel> =
    map { mapOrgDbToDomModel(it) }

fun List<ObjectDto>.toInetNumEntities(id: Int): List<InetNumDomModel> =
    map { mapDtoToDomModel(it, id) }

fun List<InetNumDbModel>.toInetNumEntities(): List<InetNumDomModel> =
    map { mapInetNumDtoToDomModel(it) }


fun mapInetNumDtoToDomModel(dto: InetNumDbModel): InetNumDomModel {

    return InetNumDomModel(
        inetNum = dto.inetNum,
        netName = dto.netName,
        countryFlag = dto.countryFlag
    )
}

fun mapOrgDbToDomModel(oDb: OrganizationDbModel): OrganizationDomModel {
    return OrganizationDomModel(
        organization = oDb.organization,
        orgName = oDb.orgName,
        countryFlag = oDb.countryFlag,
        address = oDb.address?:"",
        phone = oDb.phone?:"",
        faxNo = oDb.faxNo?:""
    )
}

fun mapDtoToDomModel(dto: ObjectDto, id: Int): InetNumDomModel {
    var countryUrl = ""
    var checkCountry = false
    var inetNum = ""
    var netName = ""
    dto.mAttributes.mAttribute.forEach()
    {

        when (it.mName) {

            "inetnum" -> inetNum = it.mValue
            "netname" -> netName = it.mValue
            "country" -> {
                checkCountry = true
                countryUrl = "https://flagsapi.com/${it.mValue}/shiny/64.png"
            }

            else -> ""
        }
    }
    return InetNumDomModel(
        inetNum = inetNum,
        netName = netName,
        countryFlag = if (checkCountry) countryUrl else null
    )
}

fun mapDtoToDomModel(dto: ObjectDto): OrganizationDomModel {
    var countryUrl = ""
    var checkCountry = false
    var orgName = ""
    var organization = ""
    var address = ""
    var phone = ""
    var faxNo = ""
    dto.mAttributes.mAttribute.forEach()
    {
        when (it.mName) {
            "organisation" -> {
                organization = it.mValue
            }

            "address" -> address += it.mValue + "\n"
            "phone" -> phone = it.mValue
            "fax-no" -> faxNo = it.mValue
            "org-name" -> orgName = it.mValue
            //"org" -> orgName = it.mValue
            "country" -> {
                checkCountry = true
                countryUrl = "https://flagsapi.com/${it.mValue}/shiny/64.png"
            }

            else -> ""
        }
    }
    return OrganizationDomModel(
        orgName = orgName,
        organization = organization,
        address = address,
        phone = phone,
        faxNo = faxNo,
        countryFlag = if (checkCountry) countryUrl else null
    )
}




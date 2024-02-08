package ama.ripe.search.data.mapper

import ama.ripe.search.data.database.models.InetNumDbModel
import ama.ripe.search.data.database.models.OrganizationDbModel
import ama.ripe.search.data.network.model.ObjectDto
import ama.ripe.search.domain.entity.InetNumDomModel
import ama.ripe.search.domain.entity.OrganizationDomModel


private const val EMPTY_STRING = ""
private const val COUNTRY_URL = "https://flagsapi.com/%s/shiny/64.png"
private const val PARAM_INETNUM = "inetnum"
private const val PARAM_NETNAME = "netname"
private const val PARAM_COUNTRY = "country"
private const val PARAM_ORGANIZATION = "organisation"
private const val PARAM_ADDRESS = "address"
private const val PARAM_PHONE = "phone"
private const val PARAM_FAX_NO = "fax-no"
private const val PARAM_ORG_NAME = "org-name"
private const val STRING_NEW_LINE = "\n"


fun List<ObjectDto>.toDbs(): List<OrganizationDbModel> = map { mapDtoToDbModel(it) }
fun List<ObjectDto>.toInetNumDbs(id: Int): List<InetNumDbModel> =
    map { mapDtoToInetNumDbModel(it, id) }


fun mapDtoToInetNumDbModel(dto: ObjectDto, id: Int): InetNumDbModel {
    var countryUrl = EMPTY_STRING
    var inetNum = EMPTY_STRING
    var netName = EMPTY_STRING
    var checkCountry = false
    dto.mAttributes.mAttribute.forEach()
    {
        when (it.mName) {

            PARAM_INETNUM -> inetNum = it.mValue
            PARAM_NETNAME -> netName = it.mValue
            PARAM_COUNTRY -> {
                checkCountry = true
                countryUrl = String.format(COUNTRY_URL, it.mValue)
            }

            else -> EMPTY_STRING
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
    var countryUrl = EMPTY_STRING
    var orgName = EMPTY_STRING
    var checkCountry = false
    var organization = EMPTY_STRING
    var address = EMPTY_STRING
    var phone = EMPTY_STRING
    var faxNo = EMPTY_STRING
    dto.mAttributes.mAttribute.forEach()
    {

        when (it.mName) {
            PARAM_ORGANIZATION -> {
                organization = it.mValue
            }

            PARAM_ADDRESS -> address += it.mValue + STRING_NEW_LINE
            PARAM_PHONE -> phone = it.mValue
            PARAM_FAX_NO -> faxNo = it.mValue
            PARAM_ORG_NAME -> orgName = it.mValue
            //"org" -> orgName = it.mValue
            PARAM_COUNTRY -> {
                checkCountry = true
                countryUrl = String.format(COUNTRY_URL, it.mValue)
            }

            else -> EMPTY_STRING
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

fun List<ObjectDto>.dtoToInetNUMEntities(): List<InetNumDomModel> =
    map { mapInetNumDtoToDomModel(it) }

fun List<InetNumDbModel>.toInetDomEntities(): List<InetNumDomModel> =
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
        address = oDb.address ?: EMPTY_STRING,
        phone = oDb.phone ?: EMPTY_STRING,
        faxNo = oDb.faxNo ?: EMPTY_STRING
    )
}

fun mapInetNumDtoToDomModel(dto: ObjectDto): InetNumDomModel {
    var countryUrl = EMPTY_STRING
    var checkCountry = false
    var inetNum = EMPTY_STRING
    var netName = EMPTY_STRING
    dto.mAttributes.mAttribute.forEach()
    {

        when (it.mName) {

            PARAM_INETNUM -> inetNum = it.mValue
            PARAM_NETNAME -> netName = it.mValue
            PARAM_COUNTRY -> {
                checkCountry = true
                countryUrl = String.format(COUNTRY_URL, it.mValue)
            }

            else -> EMPTY_STRING
        }
    }
    return InetNumDomModel(
        inetNum = inetNum,
        netName = netName,
        countryFlag = if (checkCountry) countryUrl else null
    )
}

fun mapDtoToDomModel(dto: ObjectDto): OrganizationDomModel {
    var countryUrl = EMPTY_STRING
    var checkCountry = false
    var orgName = EMPTY_STRING
    var organization = EMPTY_STRING
    var address = EMPTY_STRING
    var phone = EMPTY_STRING
    var faxNo = EMPTY_STRING
    dto.mAttributes.mAttribute.forEach()
    {
        when (it.mName) {
            PARAM_ORGANIZATION -> {
                organization = it.mValue
            }

            PARAM_ADDRESS -> address += it.mValue + STRING_NEW_LINE
            PARAM_PHONE -> phone = it.mValue
            PARAM_FAX_NO -> faxNo = it.mValue
            PARAM_ORG_NAME -> orgName = it.mValue
            //"org" -> orgName = it.mValue
            PARAM_COUNTRY -> {
                checkCountry = true
                countryUrl = String.format(COUNTRY_URL, it.mValue)
            }

            else -> EMPTY_STRING
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



